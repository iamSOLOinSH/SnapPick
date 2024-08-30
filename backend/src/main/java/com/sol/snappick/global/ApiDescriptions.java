package com.sol.snappick.global;

public class ApiDescriptions {

    public static class MemberController {

        public static final String des_SimpleMemberInfoRes = """
                \n
                <b>SimpleMemberInfoRes</b>
                \n
                | Name | Type | Description |
                |-----|-----|-------|
                | name | string | 이름 |
                | imageUrl | string | 프로필 이미지 링크 |
                | role | <u>string</u> | 구매자 혹은 판매자 |
                \n
                """;

    }

    public static class AccountController {
        public static final String des_AccountSingleReq = """
                \n
                <b>AccountSingleReq</b>
                \n
                | Name | Type | Description |
                |-----|-----|-------|
                | accountNumber | string | 계좌번호 |
                \n
                """;

        public static final String des_string_date = """
                \n
                <b>date</b>
                \n
                | 조회날짜 | 8글자 | ex.20240830
                \n
                """;

        public static final String des_AccountStateRes = """
                \n
                <b>AccountStateRes</b>
                \n
                | Name | Type | Description |
                |-----|-----|-------|
                | bankName | string | 은행명 |
                | accountNumber | string | 계좌번호 |
                | theBalance | long | 현재 잔액 |
                \n
                """;

        public static final String des_AccountTransferReq = """
                \n
                <b>AccountTransferReq</b>
                \n
                | Name | Type | Description |
                |-----|-----|-------|
                | accountNumber | string | 계좌번호 |
                | balance | long | 이체 금액 |
                \n
                """;


        public static final String des_IdentificationReq = """
                \n
                <b>IdentificationReq</b>
                \n
                | Name | Type | Description |
                |-----|-----|-------|
                | accountNumber | string | 계좌번호 |
                | authCode | varchar(4) | 인증코드 4글자 |
                \n
                """;

    }

    public static class common {
        public static final String des_input = """
                \n            
                <b> Input : </b>
                \n
                """;

        public static final String des_output = """
                \n            
                <b> Output : </b>
                \n
                """;
    }

    public static class Headers {
        public static final String des_header_token = """
                \n
                <b> Header : </b>
                \n
                | key | value  | 
                |-----|-----|
                | Authorization | Bearer ~ | 
                \n
                <hr/> 
                \n
                """;
        public static final String des_MemberRegisterReq = """
                \n
                <b>MemberRegisterReq</b>
                \n
                | Name | Type  | Description |
                |-----|-----|-------|
                | role | int | 구매자(0) 혹은 판매자(1) |
                | phoneNumber | varchar(15) | 핸드폰 번호 |
                | businessNumber | varchar(20) |  사업자 등록 번호(선택) |
                | pinCode | varchar(4) |  간편 비밀번호 |
                \n
                """;
    }
}
