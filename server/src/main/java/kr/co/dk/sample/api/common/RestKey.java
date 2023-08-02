package kr.co.dk.sample.api.common;

public interface RestKey {

    /**
     * S3
     */
    String RESION_NAME = "ap-northeast-2";
    String ADMIN_ACCESS_KEY = "AKIA2U5GLA257EJQ455Q";
    String ADMIN_SECRET_KEY = "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";
    int SIGNED_URL_EXPIRED_TIME = 1000 * 60;


    String NAME_USER_ID = "user_id";
    String NAME_USER_PW = "user_pw";
    String NAME_PAGE = "page";

    String NAME_SESSION_USER_IDX = "idx";
    String NAME_SESSION_USER_NAME = "user_name";

    String NAME_USER_IDX = "user_idx";
    String NAME_OFFSET = "offset";
    String NAME_COUNT = "count";
    String NAME_STATUS = "status";
    String NAME_CONTRACT_NAME = "c_name";
    String NAME_CONTRACT_TO_USER = "to_user";
    String NAME_CONTRACT_FROM_USER = "from_user";
    String NAME_CONTRACT_PRICE = "c_price";
    String NAME_CONTRACT_DATE_RANGE = "c_date_range";
    String NAME_CONTRACT_CONTENT = "c_content";
    String NAME_CONTRACT_FILE = "c_file";
    String NAME_CONTRACT_TXID = "txid";
    String NAME_CONTRACT_START_DATE = "start_date";
    String NAME_CONTRACT_END_DATE = "end_date";
    String NAME_CONTRACT_REG_DATE = "reg_date";
    String NAME_CONTRACT_ACT_TXID = "act_txid";
    String NAME_CONTRACT_USER_ROLE = "user_role";
    String NAME_CONTRACT_IDX = "c_idx";
    String NAME_CONTRACT_IS_READ = "is_read";

    int PAGE_ROWS = 10;

    String RESULT_LIST = "list";
    String RESULT_TOTAL_CNT = "total_cnt";
    String RESULT_UNREAD_CNT = "unread_cnt";
    String RESULT_FILE_URL = "file_url";

    String CONTRACT_USER_INFO = "users";
    String TO_USER_INFO = "to_user_info";
    String FROM_USER_INFO = "from_user_info";
    String CONTRACT_DOC_HASH = "contract_doc_hash";
    String ACT_USER = "act_user";
    String ACT_TYPE = "act_type";
    String ACT_TYPE_REGIEST = "REG";
    String ACT_TYPE_REJECT = "REJ";
    String ACT_TYPE_CONFIRM = "CON";

    String RES_ORIGIN_HASH = "origin_hash";
    String RES_CHECK_HASH = "check_hash";


    int TYPE_GAB = 2;
    int TYPE_UEL = 3;

}
