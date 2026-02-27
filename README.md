# DevBlog: 技術ブログ / Tech Blog Platform

個人技術ブログプラットフォームであり、Spring Boot 3.5.10を基盤として構築されたフルスタックのウェブアプリケーションです。

---

## 📌 プロジェクト概要

DevBlogは、開発者が技術的な知識と経験を共有できるMarkdownベースのテックブログプラットフォームです。

### 主な特徴
- ✅ **ユーザー認証システム**: ログインIDに基づく会員登録およびログイン
- ✅ **投稿管理**: マークダウンエディターを活用した投稿の作成・編集・削除
- ✅ **画像アップロード**: 投稿内画像のプレビューおよび個別削除機能
- ✅ **コメントシステム**: 投稿ごとのコメント作成および管理
- ✅ **いいね機能**: 投稿ごとのリアルタイムいいねカウント
- ✅ **タグ分類**: タグに基づく投稿の検索およびフィルタリング
- ✅ **管理者ダッシュボード**: ユーザーおよび投稿管理機能
- ✅ **レスポンシブデザイン**: モバイル・タブレット・デスクトップ最適化
- ✅ **プライベート投稿**: 個人的な投稿公開の可否設定

---

## 🛠️ 技術スタック

### Backend
| 項目 | 技術 |
|------|------|
| **Framework** | Spring Boot 3.5.10 |
| **Language** | Java 17 |
| **Build Tool** | Gradle 8.x |
| **ORM** | MyBatis 3.0.5 |
| **Security** | Spring Security 6.x + BCrypt |
| **Markdown** | Flexmark |

### Frontend
| 項目 | 技術 |
|------|------|
| **Template Engine** | Thymeleaf 3.1 |
| **Editor** | Toast UI Editor (CDN) |
| **Font** | LINE Seed JP (Google Fonts) |
| **CSS** | Custom CSS (Grid, Flexbox) |
| **JavaScript** | Vanilla JS (jQuery 無し) |

### Database & Infrastructure
| 項目 | 技術 |
|------|------|
| **Database** | MySQL 8.0 |
| **Upload Storage** | Local File System |
| **IDE** | Eclipse STS　4.31.0. |
| **Server Port** | 8089 |

---

## 📂 プロジェクト構成

```
devblog/
├── src/main/java/com/devblog/
│   ├── config/              # Spring 設定 (Security, UserDetails, WebConfig)
│   ├── controller/          # HTTP リクエスト処理 (Auth, Post, Comment, Admin, Like, MyPage)
│   ├── domain/              # エンティティクラス (User, Post, Comment, Tag, Like, PostImage)
│   ├── mapper/              # MyBatis データアクセス層
│   ├── service/             # ビジネスロジック (UserService, etc.)
│   └── DevblogApplication.java
│
├── src/main/resources/
│   ├── mapper/              # MyBatis XML mapper File
│   ├── static/
│   │   ├── css/style.css    # All StyleSheet
│   │   └── js/main.js       # クライアントロジック
│   ├── templates/
│   │   ├── fragments/       # 再利用可能なThymeleafフラグメント (header, footer)
│   │   ├── auth/            # 認証ページ (login, signup)
│   │   ├── post/            # 投稿ページ (list, detail, write, edit)
│   │   ├── admin/           # 管理者ページ (dashboard, users, posts)
│   │   ├── mypage/          # マイページ (profile view, edit)
│   │   └── index.html       # メインページ
│   └── application.yml      # アプリケーション設定
│
└── upload/              
```
