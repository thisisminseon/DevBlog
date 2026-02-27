# DevBlog: 技術ブログ / Tech Blog Platform

개인 기술 블로그 플랫폼으로, Spring Boot 3.5.10을 기반으로 구축된 풀스택 웹 애플리케이션입니다.
일본 기업의 채용 요건을 고려한 포트폴리오 프로젝트입니다.

---

## 📌 프로젝트 개요

**DevBlog**는 개발자들이 기술적 지식과 경험을 공유할 수 있는 마크다운 기반의 테크 블로그 플랫폼입니다.

### 주요 특징
- ✅ **사용자 인증 시스템**: 로그인 ID 기반 회원가입 및 로그인
- ✅ **포스트 관리**: 마크다운 에디터를 활용한 게시글 작성/수정/삭제
- ✅ **이미지 업로드**: 포스트 내 이미지 미리보기 및 개별 삭제 기능
- ✅ **댓글 시스템**: 포스트별 댓글 작성 및 관리
- ✅ **좋아요 기능**: 포스트별 실시간 좋아요 카운트
- ✅ **태그 분류**: 태그 기반 포스트 검색 및 필터링
- ✅ **관리자 대시보드**: 사용자 및 포스트 관리 기능
- ✅ **반응형 디자인**: 모바일/태블릿/데스크톱 최적화
- ✅ **프라이빗 포스트**: 개인적인 포스트 공개 여부 설정

---

## 🛠️ 기술 스택

### Backend
| 항목 | 기술 |
|------|------|
| **Framework** | Spring Boot 3.5.10 |
| **Language** | Java 17 |
| **Build Tool** | Gradle 8.x |
| **ORM** | MyBatis 3.0.5 |
| **Security** | Spring Security 6.x + BCrypt |
| **Markdown** | Flexmark (서버사이드) |

### Frontend
| 항목 | 기술 |
|------|------|
| **Template Engine** | Thymeleaf 3.1 |
| **Editor** | Toast UI Editor (CDN) |
| **Font** | LINE Seed JP (Google Fonts) |
| **CSS** | Custom CSS (Grid, Flexbox) |
| **JavaScript** | Vanilla JS (jQuery 없음) |

### Database & Infrastructure
| 항목 | 기술 |
|------|------|
| **Database** | MySQL 8.0 |
| **Upload Storage** | Local File System |
| **IDE** | Eclipse STS 4.x |
| **Server Port** | 8089 |

---

## 📂 프로젝트 구조

```
devblog/
├── src/main/java/com/devblog/
│   ├── config/              # Spring 설정 (Security, UserDetails, WebConfig)
│   ├── controller/          # HTTP 요청 처리 (Auth, Post, Comment, Admin, Like, MyPage)
│   ├── domain/              # 엔티티 클래스 (User, Post, Comment, Tag, Like, PostImage)
│   ├── mapper/              # MyBatis 데이터 접근 계층
│   ├── service/             # 비즈니스 로직 (UserService, etc.)
│   └── DevblogApplication.java
│
├── src/main/resources/
│   ├── mapper/              # MyBatis XML 매퍼 파일
│   ├── static/
│   │   ├── css/style.css    # 전체 스타일시트 (1454줄)
│   │   └── js/main.js       # 클라이언트 로직
│   ├── templates/
│   │   ├── fragments/       # 재사용 가능한 Thymeleaf 프래그먼트 (header, footer)
│   │   ├── auth/            # 인증 페이지 (login, signup)
│   │   ├── post/            # 포스트 페이지 (list, detail, write, edit)
│   │   ├── admin/           # 관리자 페이지 (dashboard, users, posts)
│   │   ├── mypage/          # 마이페이지 (profile view, edit)
│   │   └── index.html       # 메인 페이지
│   └── application.yml      # 애플리케이션 설정
│
└── upload/                  # 사용자 업로드 이미지 저장소
```

---

## 🔐 핵심 기능 상세

### 1. 인증 및 사용자 관리
- **로그인 ID 방식**: username 대신 loginId로 인증
- **회원가입 검증**: loginId 중복 체크
- **ID 기억 기능**: localStorage를 활용한 로그인 ID 자동 입력
- **역할 기반 접근 제어 (RBAC)**: ADMIN, USER 역할 구분

**관련 파일**:
- `CustomUserDetailsService.java`: Spring Security 통합
- `SecurityConfig.java`: 보안 설정 (formLogin, authorizeRequests)
- `AuthController.java`: 회원가입/로그인 처리
- `UserMapper.xml`: loginId 쿼리

### 2. 포스트 관리 (CRUD)
- **작성**: Toast UI Editor를 활용한 마크다운 편집
- **읽기**: Flexmark를 통한 마크다운→HTML 변환
- **수정**: 기존 포스트 내용 불러오기 및 재편집
- **삭제**: 소유자만 가능한 삭제 권한

**특별 기능**:
- 이미지 미리보기: DataTransfer API를 활용한 파일 관리
- 이미지 개별 삭제: 호버 시 나타나는 X 버튼
- 프라이빗 포스트: 공개 여부 토글 스위치
- LONGTEXT 컬럼: 장문 포스트 지원

**관련 파일**:
- `PostController.java`: CRUD 로직 + 이미지 처리
- `PostMapper.xml`: 데이터베이스 쿼리
- `write.html`, `edit.html`: 에디터 UI
- `main.js`: 이미지 프리뷰 로직

### 3. 댓글 시스템
- 포스트별 댓글 작성/수정/삭제
- 댓글 작성자 표시
- 타임스탬프 자동 기록

**관련 파일**:
- `CommentController.java`
- `CommentMapper.xml`

### 4. 좋아요 기능
- 포스트별 실시간 좋아요 카운팅
- 중복 좋아요 방지
- AJAX를 활용한 비동기 처리

**관련 파일**:
- `LikeController.java`
- `PostLikeMapper.xml`

### 5. 태그 시스템
- 포스트 작성 시 태그 추가 (Chip 형식)
- Hero 섹션에 모든 태그 표시
- 태그 클릭으로 필터링된 포스트 목록 보기

**관련 파일**:
- `TagMapper.xml`
- `write.html` 태그 입력 컴포넌트

### 6. 관리자 대시보드
- **통계**: 전체 사용자 수, 포스트 수 (가로 배치 카드)
- **사용자 관리**: 사용자 목록 및 역할 변경
- **포스트 관리**: 모든 포스트 조회 및 삭제

**관련 파일**:
- `AdminController.java`
- `admin/index.html`, `users.html`, `posts.html`

---

## 🎨 UI/UX 설계

### 디자인 철학
- **미니멀리즘**: 불필요한 요소 제거, 핵심 기능 강조
- **반응형**: 모바일(480px), 태블릿(768px), 데스크톱 최적화
- **접근성**: 명확한 시각 계층, 충분한 색상 대비

### 색상 체계
- **Primary**: Indigo (#6366f1)
- **Background**: White (#fff), Light Gray (#f9fafb)
- **Text**: Dark Gray (#1a1a1a), Medium Gray (#666)
- **Accent**: Red (#dc2626) for danger, Green (#16a34a) for success

### 주요 UI 컴포넌트
1. **Header**: Sticky, 네비게이션 링크, 로그인/로그아웃 버튼
2. **Hero Section**: 우주/밤하늘 테마 (CSS 별 애니메이션), 태그 버튼
3. **Post Card**: 썸네일, 제목, 설명, 메타데이터, 태그
4. **Button Variants**: Primary, Secondary, Danger, Outline, Small
5. **Form Controls**: 통일된 input/textarea 스타일, 토글 스위치
6. **Footer**: Sticky bottom, GitHub/Email 링크

---

## 🚀 주요 구현 내용

### Phase 1-9: 기본 기능 (이전 완성)
- ✅ Spring Boot 프로젝트 초기 설정
- ✅ 사용자 인증 시스템
- ✅ 포스트 CRUD 기능
- ✅ 댓글 및 좋아요 시스템
- ✅ 태그 분류 기능
- ✅ 관리자 대시보드

### Phase 10: 로그인 ID 시스템
**문제**: 일반적인 username 대신 loginId로 인증하고 싶음

**구현 내용**:
1. User 도메인에 `loginId` 필드 추가
2. UserMapper에 `findByLoginId()` 쿼리 추가
3. CustomUserDetailsService에서 loginId로 사용자 조회
4. SecurityConfig에 `.usernameParameter("loginId")` 설정
5. AuthController에서 loginId 중복 체크
6. 로그인/회원가입 HTML에 loginId 필드 추가

**결과**: 사용자는 username 대신 고유한 loginId로 로그인 ✅

### Phase 11: UI 통일 및 버튼 크기 개선
**문제**: 헤더 버튼, 편집/삭제 버튼, stat-card 크기가 제각각

**구현 내용**:
1. `.header-btn` 클래스 통일: `height: 36px`, `box-sizing: border-box`, `line-height: 1`
2. `.btn-sm` 개선: `height: 32px` 명시, `<a>`와 `<button>` 크기 동일화
3. `.btn-secondary`에 투명 border 추가 (`.btn-danger` border와 동일화)
4. `.post-action-links`에 `align-items: center` 추가

**결과**: 모든 버튼의 높이와 크기가 통일됨 ✅

### Phase 12: Hero 섹션 재설계
**문제**: 기존 단순 배경에서 더 시각적으로 매력적인 디자인 필요

**구현 내용**:
1. **우주/밤하늘 테마**: `linear-gradient`로 그라데이션 배경
2. **별 애니메이션**: `::before`, `::after` 의사 요소로 2개 별 레이어
   - Layer 1: 작은 흰색 별 (dim), 3초 twinkle 애니메이션
   - Layer 2: 큰 별 (밝음) + 색상별 별 (파랑, 노랑, 보라), 2.5초 twinkle
3. **태그 floating**: 각 태그에 별도의 애니메이션 (float up/down)
4. **텍스트 색상**: 완전 흰색 (#fff)

**관련 CSS**:
```css
.hero::before, .hero::after {
  radial-gradient로 1px~3px 별 생성
  animation: twinkle1/twinkle2 적용
}
.hero-tag {
  animation: floatTag 적용, nth-child 지연 설정
}
```

**결과**: 매력적인 우주 테마의 Hero 섹션 ✅

### Phase 13: 이미지 프리뷰 삭제 기능
**문제**: 포스트 작성 시 업로드된 이미지를 선택 해제하기 어려움

**구현 내용**:
1. **DataTransfer API**: 파일 input의 files를 동적으로 관리
2. **selectedFiles 배열**: 현재 선택된 파일 추적
3. **renderImagePreviews()**: 각 파일 미리보기 렌더링 + 삭제 버튼
4. **removePreviewImage()**: 배열에서 파일 제거 후 재렌더링
5. **syncFilesToInput()**: DataTransfer로 input.files 동기화
6. **CSS hover 효과**:
   ```css
   .image-preview-item .remove-preview { opacity: 0; }
   .image-preview-item:hover .remove-preview { opacity: 1 !important; }
   ```

**결과**: 이미지 미리보기 호버 시 X 버튼 출현 및 삭제 가능 ✅

### Phase 14: 관리자 대시보드 가로 배치
**문제**: stat-card가 세로로 쌓이는 문제

**구현 내용**:
1. `.stats-grid`: `display: grid` → `display: flex` 변경
2. 각 `.stat-card`: `flex: 1 1 0` (동일 너비)
3. `min-width: 200px`, `max-width: 400px` 제약
4. **네비게이션 버튼**: 별도 `admin-nav` div 대신 각 카드 내부에 배치
5. 반응형: 768px 이하에서 2열, 480px 이하에서 1열

**결과**: stat-card가 가로로 나란히 배치, 각 카드 아래 해당 버튼 위치 ✅

### Phase 15: 기술 부채 해결
**문제들**:
- Eclipse `-parameters` 컴파일러 설정으로 인한 `@PathVariable` 오류
- 숨겨진 textarea의 `required` 속성으로 form 제출 불가
- MySQL `content` 컬럼 크기 부족
- footer가 페이지 중간에 떠있음

**구현 내용**:
1. **@PathVariable 명시**: 모든 `@PathVariable`에 `("id")` 명시
2. **textarea required 제거**: hidden textarea에서 required 삭제
3. **MySQL ALTER**: `ALTER TABLE post MODIFY content LONGTEXT;`
4. **Sticky Footer**:
   ```css
   body { display: flex; flex-direction: column; min-height: 100vh; }
   .footer { margin-top: auto; }
   ```

**결과**: 모든 기술 부채 해결 ✅

### Phase 16: Footer 개선
**구현 내용**:
1. GitHub 링크: `https://github.com/thisisminseon` 설정
2. Portfolio 섹션 제거
3. Email 추가: 클릭 시 Toast 알림으로 이메일 표시
4. Clipboard Copy API: `navigator.clipboard.writeText()` 사용

**결과**: Footer가 깔끔하고 실용적으로 개선됨 ✅

---

## 🔧 설정 및 실행

### 필수 요구사항
- Java 17 이상
- MySQL 8.0 이상
- Gradle 8.x 이상

### 데이터베이스 설정
```sql
CREATE DATABASE jsl26db;
CREATE USER 'jsl26'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON jsl26db.* TO 'jsl26'@'localhost';
FLUSH PRIVILEGES;
```

### 애플리케이션 실행
```bash
# 프로젝트 디렉토리
cd /Users/parkminseon/Desktop/SpringBoot/devblog

# Gradle로 실행
./gradlew bootRun

# 또는 IDE에서 DevblogApplication.java 실행
```

**접근 URL**: `http://localhost:8089/hello`

### 관리자 계정 생성
```sql
-- 일반 사용자로 가입 후 실행
UPDATE users SET role = 'ADMIN' WHERE id = 1;
```

---

## 📊 데이터베이스 스키마

### 주요 테이블
```sql
-- 사용자
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  login_id VARCHAR(50) UNIQUE NOT NULL,
  username VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(20) DEFAULT 'USER'
);

-- 포스트
CREATE TABLE post (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  content LONGTEXT NOT NULL,
  author VARCHAR(100) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_private BOOLEAN DEFAULT FALSE
);

-- 댓글
CREATE TABLE comment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  post_id BIGINT NOT NULL,
  author VARCHAR(100) NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 좋아요
CREATE TABLE post_like (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  post_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  UNIQUE KEY (post_id, user_id)
);

-- 태그
CREATE TABLE tag (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL
);

-- 포스트-태그 연관
CREATE TABLE post_tag (
  post_id BIGINT NOT NULL,
  tag_id BIGINT NOT NULL,
  PRIMARY KEY (post_id, tag_id)
);

-- 포스트 이미지
CREATE TABLE post_image (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  post_id BIGINT NOT NULL,
  image_path VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🧪 테스트 및 검증

### 수동 테스트 체크리스트
- [ ] **회원가입**: loginId 중복 체크, 패턴 검증
- [ ] **로그인**: ID 기억 체크박스 작동, localStorage 저장
- [ ] **포스트 작성**: 마크다운 에디터, 이미지 업로드, 태그 추가
- [ ] **이미지 관리**: 미리보기 렌더링, 호버 삭제 버튼, 개별 삭제
- [ ] **포스트 수정**: 기존 내용 로드, 수정 반영
- [ ] **댓글**: 작성, 수정, 삭제
- [ ] **좋아요**: 카운트 증감, 중복 방지
- [ ] **태그 필터**: 태그 클릭 시 필터된 목록
- [ ] **관리자**: 대시보드 통계, 사용자/포스트 관리
- [ ] **반응형**: 모바일(375px), 태블릿(768px), 데스크톱(1440px)

---

## 🎯 배운 점 및 성과

### 기술적 성장
1. **Spring Boot 풀스택 개발**: Backend + Frontend 통합
2. **MyBatis 활용**: XML 매퍼를 통한 효율적인 데이터 접근
3. **Spring Security**: 맞춤형 인증 시스템 (loginId 기반)
4. **Thymeleaf 템플릿**: 동적 HTML 생성 및 폼 바인딩
5. **CSS 심화**: Grid, Flexbox, Pseudo-elements, Animations
6. **JavaScript**: DataTransfer API, localStorage, AJAX
7. **데이터베이스**: MySQL 스키마 설계, ALTER 명령어

### 문제 해결
- Eclipse 컴파일러 설정 (`-parameters`) 이해 및 해결
- 브라우저 캐시 문제 해결 (CSS 버전 파라미터)
- form 제출 차단 원인 파악 (hidden textarea required)
- CSS overflow와 position absolute 상호작용 이해

### 포트폴리오 가치
- ✅ 풀스택 웹 개발 능력 증명
- ✅ 사용자 중심의 UI/UX 설계
- ✅ 마크다운 기반 CMS 구현
- ✅ 관리자 기능 포함
- ✅ 반응형 디자인
- ✅ 일본 기업 채용 기준 고려 (일본어 UI)

---

## 🚀 향후 개선 계획

### 단기 (Near-term)
- [ ] 검색 기능 고도화 (전문 검색, 제목/내용 검색)
- [ ] 댓글 페이지네이션
- [ ] 포스트 조회수 기록
- [ ] 사용자 프로필 이미지
- [ ] 다크 모드 토글

### 중기 (Mid-term)
- [ ] 포스트 공개/비공개 권한 세분화
- [ ] 팔로우 시스템
- [ ] 실시간 알림 (WebSocket)
- [ ] 게시글 추천 알고리즘
- [ ] Elasticsearch를 활용한 전문 검색

### 장기 (Long-term)
- [ ] AWS/GCP 배포
- [ ] Docker 컨테이너화
- [ ] API 문서 (Swagger/OpenAPI)
- [ ] 마이크로서비스 아키텍처
- [ ] CI/CD 파이프라인 (GitHub Actions)

---

## 📞 문의 및 연락처

**GitHub**: https://github.com/thisisminseon
**Email**: parkms9706@gmail.com

---

## 📄 라이센스

이 프로젝트는 개인 포트폴리오 목적으로 제작되었습니다.

---

## 🙏 감사의 말

Spring Boot, Thymeleaf, MyBatis, Toast UI Editor 등 오픈소스 커뮤니티에 감사합니다.

---

**최종 업데이트**: 2024년 2월
**프로젝트 상태**: 활발한 개발 중 🚀
