<template>
  <div></div>
</template>

<script>
import { mapActions, mapState} from "vuex";
import Constant from "@/Constant";

const userStore = "UserStore";

export default {
  name: "GoogleLogin",
  computed: {
    ...mapState(userStore, ["isLogin", "isLoginError", "userInfo"]),
  },
  methods: {
    ...mapActions(userStore, [Constant.GET_USER_INFO, Constant.USER_LOGIN_GOOGLE]),
  },
  mounted() {
    let self = this;
    console.log(this.$route.query);
    try {
      if (this.$route.query.code.length !== undefined) {
        console.log("code:",this.$route.query.code);
        var callbackFuc = async () => {
          let body = {
            code: `${self.$route.query.code}`,
          }
          await this.userLoginGoogle({ body });
          let token = sessionStorage.getItem("access-token");
          if (this.isLogin) {
            await this.getUserInfo(token);
            this.$router.push({ name: "main" });
          }
        }
        callbackFuc();
      }
    } catch (e){
      console.log(e);
    }
  },
}
</script>

<style>

</style>