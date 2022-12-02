<template>
  <div class="container sticky-top zindex-sticky top-0">
    <div class="row">
      <div class="col-12">
        <nav
          class="blur navbar navbar-expand-lg top-0 zindex-fixed shadow position-absolute my-3 py-2 start-0 end-0 w-100"
          style="background-color: #f5efe6"
        >
          <a class="navbar-brand ml-3 mr-5 logo" href="/" style="color: #4b555e; font-size: 2em"
            >Home</a
          >
          <button
            class="navbar-toggler"
            type="button"
            data-toggle="collapse"
            data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <b-icon-list></b-icon-list>
          </button>

          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
              <li class="nav-item" @click="changed">
                <router-link id="apt" to="/apt" class="ml-3 mr-3 nav-link text-secondary"
                  >아파트 검색</router-link
                >
              </li>
              <li class="nav-item" @click="changed">
                <router-link id="notice" to="/notice" class="ml-3 mr-3 nav-link text-secondary"
                  >공지사항</router-link
                >
              </li>
              <li class="nav-item" @click="changed">
                <router-link id="qna" to="/qna" class="ml-3 mr-3 nav-link text-secondary"
                  >QnA</router-link
                >
              </li>
            </ul>
            <ul class="navbar-nav ml-auto">
              <li class="mr-2" v-if="userInfo != null">
                <a
                  class="nav-link"
                  href="#"
                  id="navbarDropdown"
                  role="button"
                  data-toggle="dropdown"
                  aria-haspopup="true"
                  aria-expanded="false"
                >
                  <b-iconstack font-scale="2">
                    <b-icon stacked icon="bell" font-scale="2"></b-icon>
                    <b-icon
                      v-if="links.length > 0"
                      stacked
                      icon="circle-fill"
                      shift-v="4"
                      shift-h="3"
                      scale="0.4"
                      style="color: red"
                    ></b-icon>
                  </b-iconstack>
                </a>
                <user-alarm
                  class="dropdown-menu dropdown-bell dropdown-menu-right dropdown-menu-bell"
                  aria-labelledby="navbarDropdown"
                >
                </user-alarm>
              </li>
              <li class="nav-item dropdown">
                <a
                  class="nav-link dropdown-toggle"
                  href="#"
                  id="navbarDropdown"
                  role="button"
                  data-toggle="dropdown"
                  aria-haspopup="true"
                  aria-expanded="false"
                >
                  <b-icon icon="people" font-scale="2"></b-icon>
                </a>
                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                  <router-link id="userpage" to="/userpage" class="dropdown-item" v-if="userInfo">
                    마이페이지
                  </router-link>
                  <router-link id="join" to="/signup" class="dropdown-item" v-if="!userInfo">
                    회원가입
                  </router-link>
                  <div class="dropdown-divider" style="border: #7895b2 1px"></div>
                  <a href="" v-if="userInfo" class="dropdown-item" @click="userLogout(userInfo.id)">
                    <b-icon icon="key"></b-icon> 로그아웃
                  </a>
                  <router-link v-else id="login" to="/login" class="dropdown-item">
                    <b-icon icon="key"></b-icon> 로그인
                  </router-link>
                </div>
              </li>
            </ul>
          </div>
        </nav>
      </div>
    </div>
  </div>
</template>

<script>
import Constant from "@/Constant";
import UserAlarm from "@/components/userpage/UserAlarm.vue";
import { mapActions, mapState } from "vuex";

const userStore = "UserStore";
const qnaStore = "QnaStore";

export default {
  name: "TheHeaderNavbar",
  data() {
    return {
      selected: "",
    };
  },
  components: {
    UserAlarm,
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
    ...mapState(qnaStore, ["links"]),
  },
  methods: {
    changed() {
      let hostIndex = location.href.indexOf(location.host) + location.host.length;
      let end = location.href.indexOf("/", hostIndex + 1);
      if (end == -1) end = location.href.length;
      let contextRoot = location.href.substring(hostIndex + 1, end);

      if (this.selected != "") document.getElementById(this.selected).classList.remove("activate");
      document.getElementById(contextRoot).classList.add("activate");
      this.selected = contextRoot;
    },
    ...mapActions(userStore, [Constant.USER_LOGOUT]),
  },
  created() {
    console.log(this.userInfo);
  },
};
</script>

<style scoped>
.nav-link,
.dropdown-item {
  text-decoration: none;
  color: #4b555e !important;
  font-weight: bold;
}
.nav-link:hover {
  background-color: #e8dfca;
  border-radius: 5px;
}
.dropdown-menu {
  background-color: #aebdca;
}
.dropdown-item:hover,
.dropdown-item:active {
  background-color: #f5efe6;
}
.activate {
  border: 2px #aebdca;
  background-color: #aebdca;
  color: black !important;
  font-weight: bold;
  border-radius: 5px;
}
.blur {
  box-shadow: inset 0px 0px 2px #fefefed1;
  -webkit-backdrop-filter: saturate(200%) blur(30px);
  backdrop-filter: saturate(200%) blur(30px);
  background-color: rgba(245, 239, 230, 0.8) !important;
}
.dropdown-menu-bell {
  right: 5em !important;
  top: 3.85em !important;
}
.dropdown-bell {
  background-color: #f5efe6 !important;
}
</style>
