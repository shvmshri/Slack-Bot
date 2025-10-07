# 🤖 Jenkins Build Monitor - Slack Bot

A Slack bot that monitors Jenkins build jobs and notifies team members in real-time, preventing build conflicts and improving CI/CD workflow efficiency.

[![Slack](https://img.shields.io/badge/Slack-Bot-4A154B?logo=slack)](https://slack.com/)
[![Jenkins](https://img.shields.io/badge/Jenkins-Integration-D24939?logo=jenkins)](https://www.jenkins.io/)
[![Java](https://img.shields.io/badge/Java-Spring-green?logo=spring)](https://spring.io/)

## 🎯 Problem Statement

In a shared development environment, multiple developers triggering builds on the same server simultaneously can lead to:
- ⚠️ Build conflicts and failures
- ⏱️ Wasted CI/CD resources
- 😤 Developer frustration
- 📉 Reduced productivity

## 💡 Solution

A Slack bot that provides real-time notifications about build jobs, allowing team members to:
- 👀 Watch specific servers for build activity
- 📢 Get notified when builds start/complete on watched servers
- 👥 See who else is watching a server
- 🔕 Unwatch servers when done

## ✨ Key Features

### Core Functionality
- **Real-time Build Notifications** - Instant alerts when builds start/finish on monitored servers
- **Watch Management** - Subscribe/unsubscribe to server notifications
- **Watcher Visibility** - View all users monitoring a specific server
- **Conflict Prevention** - Reduce build collisions through awareness
- **Multi-server Support** - Monitor multiple Jenkins servers simultaneously

## 🛠️ Tech Stack

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Backend Framework** | JAX-RS | RESTful API endpoints |
| **Database** | MongoDB | Store user preferences and watch lists |
| **DI Container** | Spring Core | Dependency injection and bean management |
| **Slack Integration** | Bolt Framework | Slack app development |
| **CI/CD Integration** | Jenkins API | Retrieve build job information |
| **Language** | Java | Core application logic |

## 🏗️ Architecture
