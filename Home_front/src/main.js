import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import {
  fas,
  faXmark,
  faCartShopping,
  faStore,
  faHouseMedical,
  faGraduationCap,
  faMugSaucer,
  faTrainSubway,
} from "@fortawesome/free-solid-svg-icons";
import { far, faStar } from "@fortawesome/free-regular-svg-icons";
import { fab } from "@fortawesome/free-brands-svg-icons";

import "@/api/vueBootstrap";

library.add(
  fas,
  far,
  fab,
  faXmark,
  faCartShopping,
  faStore,
  faHouseMedical,
  faGraduationCap,
  faMugSaucer,
  faTrainSubway,
  faStar
);

Vue.config.productionTip = false;
Vue.component("font-awesome-icon", FontAwesomeIcon);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
