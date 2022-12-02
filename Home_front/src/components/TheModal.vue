<template>
  <div class="black-bg d-flex justify-content-center align-items-center">
    <div class="white-bg d-flex justify-content-center container">
      <div class="modal-content">
        <div class="d-flex justify-content-end">
          <font-awesome-icon class="close" @click="$emit('close')" icon="fa-xmark" />
        </div>
        <div class="content d-flex justify-content-center align-items-center">
          <div>{{modalMessage}}</div>
        </div>
        <div class="footer">
          <button class="btn btn-light" @click="goSignup" v-if="showBtn">회원가입하러 가기</button>
          <button class="btn btn-light ml-2" @click="$emit('close')">닫기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapMutations, mapState } from "vuex";
import Constant from "@/Constant";
const userStore = "UserStore";

export default {
  name: "TheModal",
  computed: {
    ...mapState(userStore,["modalMessage","showBtn"]),
  },
  methods: {
    ...mapMutations(userStore,[Constant.SET_SHOW_MODAL]),
    goSignup() {
      this.setShowModal(false);
      this.$router.push({ name: "signup" });
    }
  }
}
</script>

<style scoped>
.black-bg{
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  position:fixed;
  z-index: 99;
}
.white-bg{
  width: 30%;
  height: 40%;
  padding-top: 10px;
  padding-bottom: 10px;
  background: white;
  border-radius: 10px;
}
.modal-content{
  padding: 10px;
  width: 100%;
  border: 0px;
}
.content{
  width: 100%;
  height: 100%;
}
.footer{
  margin-bottom: 5px;
  width: 100%;
  bottom:0;
}
</style>