<template>
  <div id="app">
    <the-header-navbar
      class="navbar--show"
      :class="{ 'navbar--hidden': !showNavbar }"
    ></the-header-navbar>
    <!-- <router-view style="margin-top: 100px"></router-view> -->
    <router-view></router-view>
    <the-footer></the-footer>
  </div>
</template>

<script>
import TheHeaderNavbar from "@/components/TheHeaderNavbar";
import TheFooter from "@/components/TheFooter";
// import ChannelService from "./ChannelService";

const OFFSET = 200;

export default {
  name: "App",
  components: {
    TheHeaderNavbar,
    TheFooter,
  },
  data() {
    return {
      showNavbar: true,
      lastScrollPosition: 0,
      scrollValue: 0,
    };
  },
  mounted() {
    // Boot Channel as an anonymous user
    // ChannelService.boot({
    //   pluginKey: "75ad5584-4a5d-4478-bf21-720e333af3f6", //please fill with your plugin key
    // });
    this.lastScrollPosition = window.pageYOffset;
    window.addEventListener("scroll", this.onScroll);
  },
  created() {
    (function (d, m) {
      var kommunicateSettings = {
        appId: "2e6d2a5d04b606d4ba9d16f08c5dc9ec0",
        popupWidget: true,
        automaticChatOpenOnNavigation: true,
      };
      var s = document.createElement("script");
      s.type = "text/javascript";
      s.async = true;
      s.src = "https://widget.kommunicate.io/v2/kommunicate.app";
      var h = document.getElementsByTagName("head")[0];
      h.appendChild(s);
      window.kommunicate = m;
      m._globals = kommunicateSettings;
    })(document, window.kommunicate || {});
  },
  methods: {
    onScroll() {
      if (window.pageYOffset < 0) {
        return;
      }
      if (Math.abs(window.pageYOffset - this.lastScrollPosition) < OFFSET) {
        return;
      }
      this.showNavbar = window.pageYOffset < this.lastScrollPosition;
      this.lastScrollPosition = window.pageYOffset;
    },
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.onScroll);
  },
};
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.navbar--show {
  top: 0;
  transition: 0.1s all ease-out;
}
.navbar--hidden {
  /* display: none; */
  top: -90px;
  /* transform: translate(0, -100%); */
}
</style>

<style>
@import url("https://fonts.googleapis.com/css2?family=Libre+Barcode+128+Text&display=swap");
@import url("https://fonts.googleapis.com/css2?family=Do+Hyeon&family=Jua&display=swap");
@font-face {
    font-family: 'SUIT-Medium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_suit@1.0/SUIT-Medium.woff2') format('woff2');
    font-weight: normal;
    font-style: normal;
}
.logo {
  font-family: "Libre Barcode 128 Text", cursive !important;
}
* {
  font-family: "SUIT-Medium", sans-serif !important;
}
</style>
