## 주문 데이터 연동

restApi를 사용한 주문 연동 시스템

## 시작 가이드

Git : https://github.com/Song-H-H/apiOrderSystem

Clone 이후 Local 8080 Port 실행

### 요구 사항

Java Version - 8 이상 ( 개발 시 Java17  )

Spring Boot version - 3.3.5

DB X

API Test - Postman 사용

### 가정

- 한 시스템 안에서 내부 외부를 가정하여 진행 ( local ↔ local )
- DB를 사용하지 않고 In-memory만 사용
    
    In-memory DB 사용이 가능한 **H2** 도 DB의 포함된다 생각하여 제한
    
    DB없이 **cache** 를 사용하기엔 의미가 없다 생각하여 제외
    
    임의의 변수 선언하여 저장 처리
    

### 설계

1. JWT
    1. 외부 요청 시 **Token** 발급으로 인한 보안 처리
2. Spring Security
    1. 인가된 사용자 판별 처리
    2. `jwtHandler` 를 통한 인가 예외 처리 추가
    3. **Token** 발급 URL 제외 인증 처리
    4. csrf, cors 제한 처리
3. Filter
    1. 발급된 토큰 정보로 **api** 요청 시 `UserDetails`  기반 `createdBy` 사용자 기입
4. Exception
    1. `@ExceptionHandler` 를 통한 예외 발생 시 Response 대응
    2. `ENUM` 처리를 통한 Exception 코드화
5. RestApi
    1. `Util` , `Template` 처리로 전송 간편화
    2. `TypeReference` 의 class Type을 받아 response Body Data casting 처리

### API 기능

InOrderControlelr : 내부

OutOrderController : 외부

모든 API는 **POST** 입니다.

1. 외부로부터 데이터를 가져와 내부에 저장
    1. `InOrderController - saveOrderDataByOut` 호출 시 `OutOrderController - makeTempData` Request 요청
    2. `OutOrderController - makeTempData` 에서 임의의 데이터 생성 후 Response
    3. 전달 받은 데이터 저장
        1. 동일한 주문 정보 전송 시 update
2. 내부에서 외부로 데이터 전달
    1. `InOrderController - sendDataToOut` 호출 시 현재 저장 된 데이터 전송
    2. `OutOrderController - loggingDataByIn` 전달 받은 데이터 출력
3. 외부에서 주문 ID로 검색
    1. `InOrderController - findByOrderNumber` Json 주문 번호 전송
    
    ```json
    request :
    {
        "searchOrderNumber" : ["140ee6ba-7169-4521-bbbd-7029b85dbc3f"
    												    ,"9ef20346-1c95-4883-9088-391d3447ce9b"]
    }
    
    respons:
      "orderList": [
          {
              "createdBy": "API_USER",
              "createdDateTime": "20241106151606",
              "lastModifiedBy": "API_USER",
              "lastModifiedDate": "20241106151606",
              "orderNumber": "140ee6ba-7169-4521-bbbd-7029b85dbc3f",
              "orderStatus": "배송 준비 중",
              "customerId": "4",
              "customerName": "customerName0",
              "orderDateTime": "20241106151606"
          },
          {
              "createdBy": "API_USER",
              "createdDateTime": "20241106151606",
              "lastModifiedBy": "API_USER",
              "lastModifiedDate": "20241106151606",
              "orderNumber": "9ef20346-1c95-4883-9088-391d3447ce9b",
              "orderStatus": "배송 준비 중",
              "customerId": "9",
              "customerName": "customerName1",
              "orderDateTime": "20241106151606"
          }
      ]
    ```
    

### Postman Test

1. getToken
    1. URL : http://localhost:8080/api/getToken
    2. body 
        
        ```json
        {
            "userId" : "API_USER",
            "password" : "2024-11-04-API-TOKEN-CREATED-USER-1"
        }
        ```
        
    3. Response
        
        ```json
        {
            "ResponseCode": 200,
            "data": {
                "userId": "API_USER",
                "password": "2024-11-04-API-TOKEN-CREATED-USER-1",
                "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBUElfVVNFUiIsImlhdCI6MTczMDg3MzU0MCwiYXV0aCI6IlJPTEUiLCJleHAiOjE3MzA4NzM4NDB9.XBX6nhQ_-5ieirrw_qvTIRPVj7cYSWYTgK8qVUEE0WA",
                "auth": true
            },
            "ResponseMessage": "OK"
        }
        ```
        
2. 외부 데이터를 가져와 저장
    1. URL : http://localhost:8080/api/in/order/saveOrderDataByOut
    2. Header : Key - Authorization, Value - Bearer ‘발급 받은 Token’
    3. Response
        
        ```json
        {
            "ResponseCode": 200,
            "data": {
                "orderList": [
                    {
                        "createdBy": "API_USER",
                        "createdDateTime": "20241106151606",
                        "lastModifiedBy": "API_USER",
                        "lastModifiedDate": "20241106151606",
                        "orderNumber": "140ee6ba-7169-4521-bbbd-7029b85dbc3f",
                        "orderStatus": "배송 준비 중",
                        "customerId": "4",
                        "customerName": "customerName0",
                        "orderDateTime": "20241106151606"
                    }
                ]
            },
            "ResponseMessage": "OK"
        }
        ```
        
3. 주문 번호 검색
    1. URL : http://localhost:8080/api/in/order/findByOrderNumber
    2. Header : Key - Authorization, Value - Bearer ‘발급 받은 Token’
    3. Body
        
        ```json
        {
            "searchOrderNumber" : ["주문번호1"
                                    ,"주문번호2"]
        }
        ```
        
    4. Response
        
        ```json
        {
            "ResponseCode": 200,
            "data": {
                "orderList": [
                    {
                        "createdBy": "API_USER",
                        "createdDateTime": "20241106151606",
                        "lastModifiedBy": "API_USER",
                        "lastModifiedDate": "20241106151606",
                        "orderNumber": "주문번호1",
                        "orderStatus": "배송 준비 중",
                        "customerId": "4",
                        "customerName": "customerName0",
                        "orderDateTime": "20241106151606"
                    },            
                    {
                        "createdBy": "API_USER",
                        "createdDateTime": "20241106151606",
                        "lastModifiedBy": "API_USER",
                        "lastModifiedDate": "20241106151606",
                        "orderNumber": "주문번호2",
                        "orderStatus": "배송 준비 중",
                        "customerId": "4",
                        "customerName": "customerName0",
                        "orderDateTime": "20241106151606"
                    }
                ]
            },
            "ResponseMessage": "OK"
        }
        ```
        
4. 고객 계정 검색
    1. URL : http://localhost:8080/api/in/order/findByCustomerId
    2. Header : Key - Authorization, Value - Bearer ‘발급 받은 Token’
    3. Body
        
        ```json
        {
            "searchCustomerId" : "고객계정" //1~10 사이의 랜덤 숫자로 생성 중
        }
        ```
        
    4. Response
        
        ```json
        {
            "ResponseCode": 200,
            "data": {
                "orderList": [
                    {
                        "createdBy": "API_USER",
                        "createdDateTime": "20241106160537",
                        "lastModifiedBy": "API_USER",
                        "lastModifiedDate": "20241106160537",
                        "orderNumber": "3b3db0d7-cfd7-4c99-b001-7b038e648d59",
                        "orderStatus": "배송 준비 중",
                        "customerId": "1",
                        "customerName": "customerName2",
                        "orderDateTime": "20241106160537"
                    },
                    {
                        "createdBy": "API_USER",
                        "createdDateTime": "20241106160704",
                        "lastModifiedBy": "API_USER",
                        "lastModifiedDate": "20241106160704",
                        "orderNumber": "aed8058c-babe-4c08-b4f9-5ff536a474a5",
                        "orderStatus": "배송 준비 중",
                        "customerId": "1",
                        "customerName": "customerName0",
                        "orderDateTime": "20241106160704"
                    }
                ]
            },
            "ResponseMessage": "OK"
        }
        ```
        
5. 주문 정보 외부로 전송
    1. URL : http://localhost:8080/api/in/order/sendDataToOut
    2. Header : Key - Authorization, Value - Bearer ‘발급 받은 Token’
    3. Resonse - 실행 console 확인
        
        ```
        	{
            "ResponseCode": 200,
            "ResponseMessage": "OK"
        }
        ```
