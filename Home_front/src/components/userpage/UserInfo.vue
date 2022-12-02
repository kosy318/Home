<template>
  <div class="container pt-5">
    <div class="row">
      <div class="col-lg-3">
        <div class="sidebar">
          <!-- User Widget -->
          <div class="card">
            <!-- User Image -->
            <div class="">
              <img src="@/assets/놀자에몽.jpg" class="rounded-circle" style="width:200px; m-auto" />
            </div>
            <!-- User Name -->
            <div class="card-body">
              <h5 class="text-center">{{ userInfo.name }}</h5>
              <p>{{ userInfo.email }}</p>
            </div>
          </div>
          <!-- Dashboard Links -->
          <div class="card m-auto">
            <div class="card-body">
              <ul class="list-unstyled">
                <li class="active">
                  <router-link class="dash-links" to="/userpage/view"><b-icon icon="person-fill"></b-icon><span> 회원정보</span></router-link
                  >
                </li>
                <li>
                  <router-link class="dash-links" to="/userpage/favorite"
                    ><b-icon icon="star-fill"></b-icon><span> 관심지역</span></router-link
                  >
                </li>
                <li>
                  <router-link class="dash-links" to="/userpage/qna"
                    ><b-icon icon="inbox-fill"></b-icon><span> 내 질문</span></router-link
                  >
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <router-view class="col-lg-9"></router-view>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

const userStore = "UserStore";

export default {
  name: "UserInfo",
  data() {
    return {};
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
  },
};
</script>

<style>
.dash-links{
  text-decoration: none;
  color: #4b555e !important;
}
</style>
