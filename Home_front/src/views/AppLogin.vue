<template>
  <div>
    <the-modal v-if="showModal" @close="setShowModal(false)" :modalMessage="modalMessage"></the-modal>
    <div class="page-header min-vh-40 relative">
      <div class="container absolute">
        <div class="row">
          <div class="col-md-7 text-center mx-auto">
            <h3><b-icon icon="key"></b-icon> 로그인</h3>
          </div>
        </div>
      </div>
    </div>
    <section class="card card-body blur shadow-blur mx-5 md-4 mt-n6 py-5">
      <form @submit.prevent="" class="text-left container-fluid grid w-50">
        <div class="form-group row">
          <label for="id" class="col-sm-3 col-form-label">아이디</label>
          <div class="col-sm-9">
            <input type="text" class="form-control" v-model="user.id" placeholder="아이디를 입력해주세요." required="" @submit.prevent="" @keyup.enter="login"/>
          </div>
        </div>
        <div class="form-group row">
          <label for="pass" class="col-sm-3 col-form-label">비밀번호</label>
          <div class="col-sm-9">
            <input type="password" class="form-control" v-model="user.pass" placeholder="비밀번호를 입력해주세요." required="" @submit.prevent="" @keyup.enter="login" />
          </div>
        </div>
        <div class="d-flex justify-content-center mt-3">
          <button class="btn btn-dark mr-2" @click="login">로그인</button>
          <button class="btn btn-dark mr-2" @submit.prevent="" @click="moveSignup">회원가입</button>
          <button class="btn btn-dark mr-2" @submit.prevent="" @click="moveFindPass">비밀번호 찾기</button>
        </div>
        <div class="d-flex justify-content-center mt-3">
          <div id="naveridlogin" @click="loginNaver" class="" style="float: left;"><img height="50" src="@/assets/btnW_완성형.png"/></div>
          <div id="googleidlogin" @click="loginGoogle" class="ml-3" style="float: left;"><img height="50" src="@/assets/btn_google_signin_light_normal_web.png"/></div>
        </div>
      </form>
    </section>
  </div>
</template>

<script>
import { mapActions, mapMutations, mapState } from "vuex";
import http from "@/api/http-common";
import TheModal from "@/components/TheModal";
import Constant from "@/Constant";

const userStore = "UserStore";
const naverClientId = process.env.VUE_APP_NAVER_LOGIN_CLIENT_ID;
const naverCallbackUri = process.env.VUE_APP_NAVER_LOGIN_REDIRECT_URL;
const googleClentId = process.env.VUE_APP_GOOGLE_LOGIN_CLIENT_ID;
const googleRedirectUri = process.env.VUE_APP_GOOGLE_LOGIN_REDIRECT_URL;


export default {
  name: "AppLogin",
  components: {
    TheModal,
  },
  data() {
    return {
      user: {
        id: "",
        pass: "",
      },
    };
  },
  computed: {
    ...mapState(userStore, ["isLogin", "isLoginError", "userInfo","showModal","modalMessage","idCheck"]),
  },
  methods: {
    ...mapActions(userStore, [Constant.USER_LOGIN, Constant.GET_USER_INFO, Constant.USER_LOGIN_NAVER]),
    ...mapMutations(userStore, [Constant.SET_SHOW_MODAL, Constant.SET_MODAL_MESSAGE, Constant.SET_SHOW_MODAL, "setShowBtn"]),
    async login() {
      await http.get(`/users/idcheck/` + this.user.id).then( async ({ data }) => {
        if (data.message == "duplicated") {
          await this.userLogin(this.user);
          let token = sessionStorage.getItem("access-token");
          if (this.isLogin) {
            await this.getUserInfo(token);
            this.$router.push({ name: "main" });
          }
        } else {
          this.setModalMessage("존재하지 않는 아이디입니다!!");
          this.setShowBtn(false);
          this.setShowModal(true);
        }
      });
    },
    moveSignup() {
      this.$router.push({ name: "signup" });
    },
    loginNaver() {
      let url = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${naverClientId}&redirect_uri=${naverCallbackUri}&state=STATE_STRING`;
      window.location.replace(url);
    },
    loginGoogle() {
      let url=`https://accounts.google.com/o/oauth2/v2/auth?client_id=${googleClentId}&redirect_uri=${googleRedirectUri}&response_type=code&scope=email%20profile%20openid&access_type=offline`
      window.location.replace(url);
    },
    moveFindPass() {
      this.$router.push({ name:"findpass"});
    }
  },
};
</script>

<style scoped>
h3 {
  font-weight: 600;
}
</style>
