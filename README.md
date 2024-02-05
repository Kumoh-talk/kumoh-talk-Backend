## DB ERD
<img width="951" alt="image" src="https://github.com/LikeLion-Kit/LikeLion-Kit-Backend/assets/96743351/f182d634-77b8-4503-87a4-486fc94af949">

## 패키지 구조
```
- domain
    |- user
	|- post
	|- post_qna
	|- announcement
	|- comment
	|- calendar

- global
	|- auth
		|- oauth
		|- token
	|- base
		|- domain
		|- dto
		|- exception
	|- config
	|- utils

```
## 커밋 규칙

| init | 프로젝트 생성 |
| --- | --- |
| enh | 새로운 기능에 대한 커밋 |
| build | 빌드 관련 파일 수정 / 모듈 설치 또는 삭제에 대한 커밋 |
| chore | 그 외 자잘한 수정에 대한 커밋 |
| docs | 문서 수정에 대한 커밋 |
| style | 코드 스타일 혹은 포맷 등에 관한 커밋 |
| refactor | 코드 리팩토링에 대한 커밋 |
| test | 테스트 코드 수정에 대한 커밋 |
| perf | 성능 개선에 대한 커밋 |