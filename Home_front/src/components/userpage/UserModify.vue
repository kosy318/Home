<template>
  <div class="text-center">
    <h5>회원정보 수정</h5>
    <br>
    <form @submit.prevent="" class="text-left container-fluid grid">
      <div class="form-group row">
        <label for="inputId" class="col-sm-2 col-form-label">아이디</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" v-model="temp.id" readonly>
        </div>
      </div>
      <div class="form-group row">
        <label for="inputName" class="col-sm-2 col-form-label">이름</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" v-model="temp.name" readonly>
        </div>
      </div>
      <div class="form-group row">
        <label for="inputPassword" class="col-sm-2 col-form-label">비밀번호</label>
        <div class="col-sm-10">
          <input type="password" class="form-control" v-model="temp.pass">
        </div>
      </div>
      <div class="form-group row">
        <label for="inputEmail" class="col-sm-2 col-form-label">이메일</label>
        <div class="col-sm-10">
          <input type="email" class="form-control" v-model="temp.email">
        </div>
      </div>
      <div class="form-group row">
        <label for="inputPhone" class="col-sm-2 col-form-label">휴대폰 번호</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" v-model="temp.phone" @keyup="getPhoneMask(temp.phone)">
        </div>
      </div>
      <div class="button-group">
        <button class="btn btn-dark mr-5" @submit.prevent="" @click="deleteUser">탈퇴하기</button>
        <button class="btn btn-dark ml-5" @submit.prevent="" @click="save">저장하기</button>
      </div>
    </form>
  </div>
  </template>
  
<script>
import http from "@/api/http-common";
import { mapActions, mapState } from "vuex"
const userStore = "UserStore"

export default {
  
  name: "UserModfiy",
  data() {
    return {
      temp: {
        id: '',
        name: '',
        pass: '',
        email:'',
        phone: '',
      },
    }
},
created() {
  console.log(this.userInfo);
  this.temp.id = this.userInfo.id;
  this.temp.name = this.userInfo.name;
  this.temp.email = this.userInfo.email;
  this.temp.phone = this.userInfo.phone;
  console.log(this.userInfo);
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
  },
  methods: {
    ...mapActions(userStore,["deleteUser"]),
    save() {
      if (this.temp.pass == this.userInfo.pass) {
        http.put(`/users`, this.temp)
          .then(() => {
            alert("수정이 완료되었습니다!!")
            this.$router.push({ name: "userview" });
        })
      } else {
        alert("비밀번호를 확인해주세요!!");
      }
    },
    getPhoneMask(val) {
        let res = this.getMask(val)
        this.contact = res
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
    }
  },
}
</script>
  
<style>
</style>