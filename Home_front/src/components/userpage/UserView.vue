<template>
<div>
  <h4>회원정보</h4>
  <br>
  <form @submit.prevent="" class="text-left container-fluid grid">
    <div class="form-group row">
      <label for="showId" class="col-sm-2 col-form-label">아이디</label>
      <div class="col-sm-10">
        <input type="text" class="form-control" v-model="userInfo.id" readonly>
      </div>
    </div>
    <div class="form-group row">
      <label for="showName" class="col-sm-2 col-form-label">이름</label>
      <div class="col-sm-10">
        <input type="text" class="form-control" v-model="userInfo.name" readonly>
      </div>
    </div>
    <div class="form-group row">
      <label for="showPassword" class="col-sm-2 col-form-label">비밀번호</label>
      <div class="col-sm-10">
        <input type="password" class="form-control" v-model="userInfo.pass" readonly>
      </div>
    </div>
    <div class="form-group row">
      <label for="showEmail" class="col-sm-2 col-form-label">이메일</label>
      <div class="col-sm-10">
        <input type="email" class="form-control" v-model="userInfo.email" readonly>
      </div>
    </div>
    <div class="form-group row">
      <label for="showPhone" class="col-sm-2 col-form-label">휴대폰 번호</label>
      <div class="col-sm-10">
        <input type="text" class="form-control" v-model="userInfo.phone" @keyup="getPhoneMask(userInfo.phone)" readonly>
      </div>
    </div>
    <button class="btn btn-dark" @submit.prevent="" @click="modify">수정하기</button>
    <button class="btn btn-dark ml-2" @submit.prevent="" @click="goChangePass">비밀번호 변경하기</button>
  </form>
</div>
</template>

<script>
import { mapState } from "vuex"

const userStore = "UserStore"

export default {
  name: "UserView",
  data() {
    return {
      image: '',
    }
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
  },
  methods: {
    modify() {
      this.$router.push({ name: "usermodify", params: { user: this.user } });
    },
    getPhoneMask(val) {
        let res = this.getMask(val)
        this.contact = res
        //서버 전송 값에는 '-' 를 제외하고 숫자만 저장
        // this.model.contact = this.contact.replace(/[^0-9]/g, '')
    },
    
    getMask(phoneNumber) {
      if (!phoneNumber) return phoneNumber
      phoneNumber = phoneNumber.replace(/[^0-9]/g, '')
      // console.log(phoneNumber);
      let res = ''
      if (phoneNumber.length < 3) {
        res = phoneNumber
      }
      else {
        if (phoneNumber.substr(0, 2) == '02') {

          if (phoneNumber.length <= 5) {//02-123-5678
            res = phoneNumber.substr(0, 2) + '-' + phoneNumber.substr(2, 3)
          }
          else if (phoneNumber.length > 5 && phoneNumber.length <= 9) {//02-123-5678
            res = phoneNumber.substr(0, 2) + '-' + phoneNumber.substr(2, 3) + '-' + phoneNumber.substr(5)
          }
          else if (phoneNumber.length > 9) {//02-1234-5678
            res = phoneNumber.substr(0, 2) + '-' + phoneNumber.substr(2, 4) + '-' + phoneNumber.substr(6)
          }

        } else {
          if (phoneNumber.length < 8) {
            res = phoneNumber
          }
          else if (phoneNumber.length == 8) {
            res = phoneNumber.substr(0, 4) + '-' + phoneNumber.substr(4)
          }
          else if (phoneNumber.length == 9) {
            res = phoneNumber.substr(0, 3) + '-' + phoneNumber.substr(3, 3) + '-' + phoneNumber.substr(6)
          }
          else if (phoneNumber.length == 10) {
            res = phoneNumber.substr(0, 3) + '-' + phoneNumber.substr(3, 3) + '-' + phoneNumber.substr(6)
          }
          else if (phoneNumber.length > 10) { //010-1234-5678
            res = phoneNumber.substr(0, 3) + '-' + phoneNumber.substr(3, 4) + '-' + phoneNumber.substr(7)
          }
        }
      }
      return res
    },
    goChangePass() {
      this.$router.push({ name: 'userchangepass' });
    }
  },
}
</script>

<style scoped>
h4{
  font-weight: 600;
}
</style>