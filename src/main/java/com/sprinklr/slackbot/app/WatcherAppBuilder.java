package com.sprinklr.slackbot.app;

import com.google.gson.reflect.TypeToken;
import com.slack.api.bolt.App;
import com.slack.api.methods.response.views.ViewsOpenResponse;
import com.slack.api.model.block.InputBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.view.View;
import com.slack.api.model.view.ViewState;
import com.sprinklr.slackbot.enums.WatcherAppSlackCommand;
import com.sprinklr.slackbot.factory.ChartReleaseDataFactory;
import com.sprinklr.slackbot.service.SlackMessageDispatcher;
import com.sprinklr.slackbot.service.WatcherCommandService;
import com.sprinklr.slackbot.util.Utils;
import com.sprinklr.slackbot.util.WatcherAppMessageConstants;
import com.sprinklr.slackbot.util.WatcherAppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.*;
import static com.slack.api.model.view.Views.*;

@Service
public class WatcherAppBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatcherAppBuilder.class);
    private static final String MODAL = "modal";
    private static final String PLAIN_TEXT = "plain_text";
    private static final String SUBMIT = "Submit";
    private static final String CANCEL = "Cancel";
    private static final String DURATION_ACTION_ID = "DURATION";
    private static final String DURATION_BLOCK_ID = "DURATION_BLOCK";
    private static final String CHART_NAME = "chartName";
    private static final String REPO_NAME = "repoName";
    private static final String DEFAULT_REPO = "Sprinklr Main App";
    private static final String REFRESH = "Refresh";
    private static final String MODULE_REFRESH = "moduleRefresh";
    private static final String PRIMARY = "primary";

    @Autowired
    SlackMessageDispatcher slackMessageDispatcher;
    @Autowired
    WatcherCommandService watcherCommandService;
    @Autowired
    ChartReleaseDataFactory chartReleaseDataFactory;

    public void addAndConfigureCommands(App app) {

        for (WatcherAppSlackCommand watcherAppSlackCommand : WatcherAppSlackCommand.values()) {
            //receive the command and present the view
            setCommand(app, watcherAppSlackCommand);

            //receive the submission of individual block actions (external data select fields)
            setBlockActions(app, watcherAppSlackCommand);

            //receive the submission of the view and extract the useful fields
            setViewSubmission(app, watcherAppSlackCommand);

            //receive "view closed" request and send some message to the slack user
            setViewClosed(app, watcherAppSlackCommand);
        }


    }

    private void setCommand(App app, WatcherAppSlackCommand watcherAppSlackCommand) {

        app.command(watcherAppSlackCommand.getCommand(), (req, ctx) -> {
            Map<String, String> metadata = new HashMap<>();
            metadata.put(REPO_NAME, DEFAULT_REPO);
            ViewsOpenResponse viewsOpenRes = ctx.client().viewsOpen(r -> r
                    .triggerId(ctx.getTriggerId())
                    .view(buildView(Utils.toJson(metadata), watcherAppSlackCommand)));
            if (viewsOpenRes.isOk()) {
                return ctx.ack();
            } else {
                LOGGER.error("Could not open the view. Error:- " + viewsOpenRes.getError());
                return ctx.ack(WatcherAppMessageConstants.FAILURE);
            }
        });

    }

    private View buildView(String privateMetadata, WatcherAppSlackCommand watcherAppSlackCommand) {

        InputBlock durationBlock = input(input -> input
                .blockId(DURATION_BLOCK_ID)
                .element(plainTextInput(pti -> pti.actionId(DURATION_ACTION_ID).placeholder(plainText("Enter duration to watch"))))
                .label(plainText(pt -> pt.text("Duration(in hours)")))
        );

        //Arraylist of different blocks of the view presented on different commands
        ArrayList<LayoutBlock> viewBlocks = new ArrayList<>();
        viewBlocks.add(section(section -> section
                .text(markdownText("Fill in the necessary details"))));
        viewBlocks.add(section(sectionBlockBuilder -> sectionBlockBuilder
                .text(markdownText("_Refresh chart names and release names"))
                .accessory(button(buttonElementBuilder -> buttonElementBuilder
                        .actionId(MODULE_REFRESH)
                        .text(plainText(REFRESH))
                        .style(PRIMARY)
                ))
        ));
        viewBlocks.add(section(section -> section
                .text(markdownText("*Repository Name*"))));
        viewBlocks.add(
                actions(actionsBlockBuilder -> actionsBlockBuilder
                        .blockId(watcherAppSlackCommand.getRepoBlockId())
                        .elements(asElements(staticSelect(staticSelectElementBuilder -> staticSelectElementBuilder
                                .actionId(watcherAppSlackCommand.getRepoActionId())
                                .placeholder(plainText("Enter the repository name. (Leave empty if it's 'Sprinklr Main App)'"))
                                .options(WatcherAppUtil.getRepoNamesObjectList())
                                .initialOption(WatcherAppUtil.getDefaultValueObject())
                        )))
                ));

        viewBlocks.add(section(section -> section
                .text(markdownText("*Chart Name*"))));
        viewBlocks.add(actions(actionsBlockBuilder -> actionsBlockBuilder
                .blockId(watcherAppSlackCommand.getChartBlockId())
                .elements(asElements(externalSelect(externalSelectElementBuilder -> externalSelectElementBuilder
                        .actionId(watcherAppSlackCommand.getChartActionId())
                        .placeholder(plainText("Select the chart name"))
                        .minQueryLength(0)
                )))
        ));
        viewBlocks.add(input(inputBlockBuilder -> inputBlockBuilder
                .blockId(watcherAppSlackCommand.getReleaseBlockId())
                .element(multiExternalSelect(multiExternalSelectElementBuilder -> multiExternalSelectElementBuilder
                        .actionId(watcherAppSlackCommand.getReleaseActionId())
                        .placeholder(plainText("Select the release names"))
                        .minQueryLength(0)
                ))
                .label(plainText("Release Names"))
        ));

        if (watcherAppSlackCommand.equals(WatcherAppSlackCommand.ADD_WATCHER)) {
            viewBlocks.add(durationBlock);
        }

        return view(view -> view
                .callbackId(watcherAppSlackCommand.getView())
                .type(MODAL)
                .notifyOnClose(true)
                .title(viewTitle(title -> title.type(PLAIN_TEXT).text(watcherAppSlackCommand.getTitle())))
                .submit(viewSubmit(submit -> submit.type(PLAIN_TEXT).text(SUBMIT)))
                .close(viewClose(close -> close.type(PLAIN_TEXT).text(CANCEL)))
                .privateMetadata(privateMetadata)
                .blocks(viewBlocks)

        );
    }
    //Called after the user has selected a value for the block

    private void setBlockActions(App app, WatcherAppSlackCommand watcherAppSlackCommand) {

        app.blockAction(watcherAppSlackCommand.getChartActionId(), (req, ctx) -> {
            Type mapType = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> metadata = Utils.fromJson(req.getPayload().getView().getPrivateMetadata(), mapType);
            metadata.put(CHART_NAME,
                    req.getPayload().getView().getState().getValues().get(watcherAppSlackCommand.getChartBlockId()).get(watcherAppSlackCommand.getChartActionId()).getSelectedOption().getText().getText());
            ctx.client().viewsUpdate(r -> r.viewId(req.getPayload().getView().getId())
                    .hash(req.getPayload().getView().getHash())
                    .view(buildView(Utils.toJson(metadata), watcherAppSlackCommand))
            );
            return ctx.ack();
        });

        app.blockAction(watcherAppSlackCommand.getRepoActionId(), (req, ctx) -> {
            Type mapType = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> metadata = Utils.fromJson(req.getPayload().getView().getPrivateMetadata(), mapType);
            metadata.put(REPO_NAME,
                    req.getPayload().getView().getState().getValues().get(watcherAppSlackCommand.getRepoBlockId()).get(watcherAppSlackCommand.getRepoActionId()).getSelectedOption().getText().getText());
            ctx.client().viewsUpdate(r -> r.viewId(req.getPayload().getView().getId())
                    .hash(req.getPayload().getView().getHash())
                    .view(buildView(Utils.toJson(metadata), watcherAppSlackCommand))
            );
            return ctx.ack();
        });


        //help to refresh chartReleaseData Cache
        app.blockAction(MODULE_REFRESH, (req, ctx) -> {
            chartReleaseDataFactory.refreshModuleInfo();
            return ctx.ack();
        });

    }

    private void setViewSubmission(App app, WatcherAppSlackCommand watcherAppSlackCommand) {

        app.viewSubmission(watcherAppSlackCommand.getView(), (req, ctx) -> {
            String userId = req.getPayload().getUser().getId();
            Map<String, Map<String, ViewState.Value>> map = req.getPayload().getView().getState().getValues();
            List<ViewState.SelectedOption> releases = map.get(watcherAppSlackCommand.getReleaseBlockId()).get(watcherAppSlackCommand.getReleaseActionId()).getSelectedOptions();

            app.executorService().submit(() -> {
                for (ViewState.SelectedOption release : releases) {
                    callSlackService(watcherAppSlackCommand, userId, map, release.getText().getText());
                }
                System.out.println(userId);
            });

            return ctx.ack();
        });

    }


    private void callSlackService(WatcherAppSlackCommand watcherAppSlackCommand, String userId, Map<String, Map<String, ViewState.Value>> map, String release) {

        String chart = map.get(watcherAppSlackCommand.getChartBlockId()).get(watcherAppSlackCommand.getChartActionId()).getSelectedOption().getText().getText();
        String message;
        switch (watcherAppSlackCommand) {

            case ADD_WATCHER:
                String duration = map.get(DURATION_BLOCK_ID).get(DURATION_ACTION_ID).getValue();
                message = watcherCommandService.handleWatchCommand(chart, release, duration, userId);
                slackMessageDispatcher.sendMessage(userId, message);
                break;

            case REMOVE_WATCHER:
                message = watcherCommandService.handleUnwatchCommand(chart, release, userId);
                slackMessageDispatcher.sendMessage(userId, message);
                break;

            case WATCHERS_LIST:
                message = watcherCommandService.handleWatcherListCommand(chart, release);
                slackMessageDispatcher.sendMessage(userId, message);
                break;

            default:
                break;

        }
    }

    private void setViewClosed(App app, WatcherAppSlackCommand watcherAppSlackCommand) {

        app.viewClosed(watcherAppSlackCommand.getView(), (req, ctx) -> ctx.ack());

    }

}