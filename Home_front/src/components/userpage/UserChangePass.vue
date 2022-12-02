<template>
  <div>
    <h4>비밀번호 변경</h4>
    <br>
    <form @submit.prevent="" class="text-left container-fluid grid">
      <div class="form-group row">
        <label for="showId" class="col-sm-3 col-form-label">현재 비밀번호</label>
        <div class="col-sm-9">
          <input type="password" class="form-control" v-model="pass" required>
        </div>
      </div>
      <br>
      <div class="form-group row">
        <label for="showName" class="col-sm-3 col-form-label">변경할 비밀번호</label>
        <div class="col-sm-9">
          <input type="password" class="form-control" v-model="change" required>
        </div>
      </div>
      <div class="row">
          <p class="col-sm-3"></p>
          <p v-if="change.length > 0 && change.length < 8" style="font-size: 14px; color: red">
            &nbsp;&nbsp;&nbsp;&nbsp;비밀번호는 8글자 이상이어야 합니다.
          </p>
        </div>
      <div class="form-group row">
        <label for="showPassword" class="col-sm-3 col-form-label">변경할 비밀번호 확인</label>
        <div class="col-sm-9">
          <input type="password" class="form-control" v-model="temp" required>
        </div>
      </div>
      <div class="row">
          <p class="col-sm-3"></p>
          <p v-if="change.length >= 8 && change == temp" style="font-size: 14px; color: blue">&nbsp;&nbsp;&nbsp;&nbsp;비밀번호가 일치합니다.</p>
          <p v-else-if="temp.length > 0" style="font-size: 14px; color: red">&nbsp;&nbsp;&nbsp;&nbsp;비밀번호가 일치하지 않습니다.</p>
        </div>
      <br>
      <button class="btn btn-dark ml-2" @submit.prevent="" @click="changePass">비밀번호 변경하기</button>
    </form>
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex"
import Constant from "@/Constant";

const userStore = "UserStore";

export default {
  name: "UserChangePass",
  data() {
    return {
      pass: '',
      change: '',
      temp: '',
    }
  },
  computed: {
    ...mapState(userStore,["userInfo"]),
  },
  methods: {
    ...mapActions(userStore,[Constant.CHANGE_USER_PASS]),
    changePass() {
      if (this.pass == this.userInfo.pass) {
        this.changeUserPass(this.change);
      } else {
        alert("비밀번호를 다시 확인해주세요!!");
      }
    },
  }
}
</script>

<style scoped>
h4{
  font-weight: 600;
}
</style>