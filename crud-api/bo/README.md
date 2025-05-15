# Application(Back Office)
Controller, Swagger, Security

DDD 원칙 위배 => 관심사 분리(각 기능은 서로의 영향을 안받고 본인 역할만 충실해야함 -> 의존성 없어야함)
api -> biz
biz -> domain
domain -> `None`

- Common 은 별개
common -> `None`

## 현재
api -> biz, domain
biz -> domain
domain -> `None`

## 해결방안??
1. api 에 biz 포함
```
api -> domain
domain -> `None`
```
2. domain 의 queryDSL 때문인데 req, res 모두 biz 에서 entity 로 변경하여 전달/반환하게 수정
   - req, res 는 biz 로 이동?? -> biz - port(in/out)?? 
3. 