// eslint-disable-next-line no-unused-vars
const naverService = {
  setNaver() {
    const naverLogin = new window.naver.LoginWithNaverId({
      clientId: process.env.VUE_APP_NAVER_CLIENT_ID,
      // callbackUrl: 'http://localhost:3015/account/naver/1', // 로컬
      callbackUrl: 'http://web.buildmomo.com/account/naver/1', // 실서버
      isPopup: false /* 팝업을 통한 연동처리 여부 */,
      loginButton: {
        color: 'green',
        type: 3,
        height: 60
      } /* 로그인 버튼의 타입을 지정 */
    })
    return naverLogin
  },
  getUserInfo() {
    const naverLogin = this.setNaver()
    naverLogin.getLoginStatus(status => {
      if (status) {
        const email = naverLogin.user.email
        const name = naverLogin.user.name
        console.log(email, name)
      } else {
        console.log('AccessToken이 올바르지 않습니다.')
      }
    })
  }
}

export default naverService
