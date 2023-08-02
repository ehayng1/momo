package kr.co.dk.sample.api.util;


import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 사용법
 * 1. 객체 불러오기
 * @Resource(name="redisUtil")
 * RedisUtil redisUtil;
 *
 * 서비스로 생성하여, 위와 같이 부르면 사용이 가능하다.
 *
 * 2. 전체 계좌 불러오기
 * redisUtil.makeRedisAccount(mb_idx);
 * 전체 계좌 정보를 담은 JSONObject 객체를 반환한다.
 *
 * 3. 특정 계좌 불러오기
 * JSONObject accountJson = redisUtil.getRedisAccount(mb_idx, CoinType.BTC);
 * BTC 게좌를 불러오는 예시
 *
 * 4. 계좌 업데이트
 * JSONObject accountJson = redisUtil.getRedisAccount("1",CoinType.BTC);
 * accountJson.replace(AccountColumn.address.getName(), "11111122222");
 * redisUtil.setRedisAccount("1", CoinType.BTC, accountJson);
 *
 * 계좌 정보를 불러와 원하는 정보에 대한 값을 치환한 후 해당 정보를 업데이트 하는 방식으로 사용한다.
 *
 * 5. KRW(원화)는 코인이 아니기 때문에 별도로 처리 한다.
 * Coin enum 을 받는 함수에서는 null 로 처리 하며, String 으로 받는 경우는 'KRW' 로 받아 처리.
 * 원화 계정은 컬럼 내용이 다르기 때문에 KrwAccountColumn 으로 컬럼 내용 봄.
 *
 *
 * 6. RDB(Mysql)의 정보를 레디스에 담는 함수 추가
 * 이외 함수와 동일한 사용 - 데이터를 DB에서 가져오기 때문에 JSONObject는 별도로 받지 않음.
 * 원화계정 역시 동일한 사용 - coin 정보에 null 값 넘기기
 */

@Slf4j
@Service("redisUtil")
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

//	@Resource(name="memberDAO")
//	private MemberDAO memberDAO;

    public boolean makeRedisAccount(String mb_idx) {
//		boolean makeResult = true;
//		try {
//
//			//원화 어카운트 생성
//			if(!redisTemplate.hasKey("KRW_" + mb_idx)) {
//				JSONObject krwMakeData = new JSONObject();
//				for(KrwAccountColumn column : KrwAccountColumn.values()) {
//					if(column.getName().equals(KrwAccountColumn.account.getName())
//							|| column.getName().equals(KrwAccountColumn.account_owner.getName())
//							|| column.getName().equals(KrwAccountColumn.bankname.getName())) {
//						krwMakeData.accumulate(column.getName(), "");
//					}else {
//						krwMakeData.accumulate(column.getName(), 0);
//					}
//				}
//				redisTemplate.opsForValue().set("KRW_"+mb_idx, krwMakeData.toString());
//			}
//			//코인별 어카운트 생성
//			for(CoinType coin : CoinType.values()) {
//				if(!redisTemplate.hasKey(coin.getName() + "_" + mb_idx)) {
//					JSONObject makeData = new JSONObject();
//					for(AccountColumn column : AccountColumn.values()) {
//						if(column.getName().equals(AccountColumn.address.getName())) {
//							makeData.accumulate(column.getName(), "");
//						}else {
//							makeData.accumulate(column.getName(), 0);
//						}
//					}
//					redisTemplate.opsForValue().set(coin.getName()+"_"+mb_idx, makeData.toString());
//				}
//
//			}
//
//		}catch(Exception e) {
//			e.printStackTrace();
//			makeResult = false;
//		}
//		return makeResult;
        return true;
    }

    // json. org 임시용으로 추가
    public boolean makeRedisAccountTmp(String mb_idx) {
//		boolean makeResult = true;
//		try {
//
//			//원화 어카운트 생성
//			if(!redisTemplate.hasKey("KRW_" + mb_idx)) {
//				org.json.JSONObject krwMakeData = new org.json.JSONObject();
//				for(KrwAccountColumn column : KrwAccountColumn.values()) {
//					if(column.getName().equals(KrwAccountColumn.account.getName())
//							|| column.getName().equals(KrwAccountColumn.account_owner.getName())
//							|| column.getName().equals(KrwAccountColumn.bankname.getName())) {
//						krwMakeData.accumulate(column.getName(), "");
//					}else {
//						krwMakeData.accumulate(column.getName(), 0);
//					}
//				}
//				redisTemplate.opsForValue().set("KRW_"+mb_idx, krwMakeData.toString());
//			}
//			//코인별 어카운트 생성
//			for(CoinType coin : CoinType.values()) {
//				if(!redisTemplate.hasKey(coin.getName() + "_" + mb_idx)) {
//					org.json.JSONObject makeData = new org.json.JSONObject();
//					for(AccountColumn column : AccountColumn.values()) {
//						if(column.getName().equals(AccountColumn.address.getName())) {
//							makeData.accumulate(column.getName(), "");
//						}else {
//							makeData.accumulate(column.getName(), 0);
//						}
//					}
//					redisTemplate.opsForValue().set(coin.getName()+"_"+mb_idx, makeData.toString());
//				}
//
//			}
//
//		}catch(Exception e) {
//			e.printStackTrace();
//			makeResult = false;
//		}
//		return makeResult;
        return true;
    }

    public JSONObject getRedisAccountAll(String mb_idx) {
        JSONObject accountAllData = null;
//		try {
//			accountAllData = new JSONObject();
//			for(CoinType coin : CoinType.values()) {
//				if(!redisTemplate.hasKey(coin.getName() + "_" + mb_idx)) {
//					makeRedisAccount(mb_idx);
//				}
//				String data = redisTemplate.opsForValue().get(coin.getName() + "_" + mb_idx);
//				JSONObject accountData = JSONObject.fromObject(data);
//				accountAllData.accumulate(coin.getName(), accountData);
//			}
//
//			//krw 별도 추가
//			if(!redisTemplate.hasKey("KRW_" + mb_idx)) {
//				makeRedisAccount(mb_idx);
//			}
//			String data = redisTemplate.opsForValue().get("KRW_" + mb_idx);
//			JSONObject accountData = JSONObject.fromObject(data);
//
//			accountAllData.accumulate("KRW",accountData);
//
//
//		}catch(Exception e) {
//			e.printStackTrace();
//			accountAllData = null;
//		}
        return accountAllData;
    }

    //org.json 임시용으로 추가
    public org.json.JSONObject getRedisAccountAllTmp(String mb_idx) {
        org.json.JSONObject accountAllData = null;
//		try {
//			accountAllData = new org.json.JSONObject();
//			for(CoinType coin : CoinType.values()) {
//				if(!redisTemplate.hasKey(coin.getName() + "_" + mb_idx)) {
//					makeRedisAccountTmp(mb_idx);
//				}
//				String data = redisTemplate.opsForValue().get(coin.getName() + "_" + mb_idx);
//				org.json.JSONObject accountData = new org.json.JSONObject(data);
//				accountAllData.accumulate(coin.getName(), accountData);
//			}
//
//			//krw 별도 추가
//			if(!redisTemplate.hasKey("KRW_" + mb_idx)) {
//				makeRedisAccountTmp(mb_idx);
//			}
//			String data = redisTemplate.opsForValue().get("KRW_" + mb_idx);
//			org.json.JSONObject accountData = new org.json.JSONObject(data);
//
//			accountAllData.accumulate("KRW",accountData);
//
//
//		}catch(Exception e) {
//			e.printStackTrace();
//			accountAllData = null;
//		}
        return accountAllData;
    }

//    public JSONObject getRedisAccount(String mb_idx, CoinType coin) {
//        return getRedisAccount(mb_idx, coin != null ? coin.getName() : null);
//    }

    public JSONObject getRedisAccount(String mb_idx, String coin) {
        JSONObject accountData = null;
        try {
            if (coin == null || coin.equals("KRW")) {
                if (!redisTemplate.hasKey("KRW_" + mb_idx)) {
                    makeRedisAccount(mb_idx);
                }

                String data = redisTemplate.opsForValue().get("KRW_" + mb_idx);

                accountData = JSONObject.fromObject(data);

            } else {
                if (!redisTemplate.hasKey(coin + "_" + mb_idx)) {
                    makeRedisAccount(mb_idx);
                }

                String data = redisTemplate.opsForValue().get(coin + "_" + mb_idx);

                accountData = JSONObject.fromObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            accountData = null;
        }
        return accountData;
    }

    public boolean applyRdbToRedis(String mb_idx, String coin) {
        boolean result = true;
        try {

            if (coin == null || coin.equals("KRW")) {
                if (!redisTemplate.hasKey("KRW_" + mb_idx)) {
                    makeRedisAccount(mb_idx);
                }
                HashMap<String, Object> paramMap = new HashMap<>();

                paramMap.put("mb_idx", mb_idx);
//                Map<String, Object> accountInfo = memberDAO.selectKRWAccount(paramMap);
                Map<String, Object> accountInfo = null;

                if (accountInfo == null)
                    return false;

                JSONObject dscAccount = new JSONObject();
                dscAccount.accumulateAll(accountInfo);

                System.out.println(accountInfo.toString());
                System.out.println(dscAccount.toString());
                redisTemplate.opsForValue().set("KRW_" + mb_idx, dscAccount.toString());

            } else { // 코인인 경우
                if (!redisTemplate.hasKey(coin + "_" + mb_idx)) {
                    makeRedisAccount(mb_idx);
                }
                HashMap<String, Object> paramMap = new HashMap<>();

                paramMap.put("coin_type", coin.toLowerCase());
                paramMap.put("mb_idx", mb_idx);

                Map<String, Object> accountInfo = null;

//                if (coin.equals("XRP")) {
//                    accountInfo = memberDAO.selectXRPAccount(paramMap);
//                } else {
//                    accountInfo = memberDAO.selectAccount(paramMap);
//                }

                if (accountInfo == null)
                    return false;

                // 값들 중 0E-8 같은 값들을 치환해주는 동작
//                for (AccountColumn column : AccountColumn.values()) {
//                    try {
//                        if (column.getType().equals(AccountColumn.TYPE_DOUBLE)) {
//                            String sValue = accountInfo.get(column.getName()) == null ? "0" : accountInfo.get(column.getName()).toString();
//                            double value = Double.parseDouble(sValue);
//                            accountInfo.put(column.getName(), value);
//                        }
//                    } catch (Exception e1) {
//                        e1.printStackTrace();
//                    }
//                }
                JSONObject dscAccount = new JSONObject();
                dscAccount.accumulateAll(accountInfo);

                redisTemplate.opsForValue().set(coin + "_" + mb_idx, dscAccount.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        log.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Finish applyRdbToRedis [mb_idx=" + mb_idx + ", coin=" + coin + "]");


        return result;
    }

    // ----------------------------------------------------------------------------
    // 2018.06.07 ADD
    // ----------------------------------------------------------------------------
    public List<Map<String, Object>> getRedisDataList(String redisKey) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            if (redisTemplate.hasKey(redisKey)) {
                String data = redisTemplate.opsForValue().get(redisKey);
                list = JsonUtil.fromJson(data, new TypeReference<List<Map<String, Object>>>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, Object> getRedisDataMap(String redisKey) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (redisTemplate.hasKey(redisKey)) {
                String data = redisTemplate.opsForValue().get(redisKey);
                map = JsonUtil.fromJson(data, new TypeReference<Map<String, Object>>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public void setRedisData(String redisKey, String value) {
        redisTemplate.opsForValue().set(redisKey, value);
    }

}
