<template>
  <div>
    <div class="page-header min-vh-40 relative">
      <div class="container absolute">
        <div class="row">
          <div class="col-md-7 text-center mx-auto">
            <h3><b-icon icon="key"></b-icon> 비밀번호 찾기</h3>
          </div>
        </div>
      </div>
    </div>
    <section class="card card-body blur shadow-blur mx-5 md-4 mt-n6 py-5">
      <form @submit.prevent="" class="text-left container-fluid grid">
        <div class="form-group row">
          <label for="text" class="col-sm-3 col-form-label">이름&nbsp;<span style="color: red">*</span></label>
          <div class="col-sm-7">
            <input type="text" class="form-control" v-model="user.name" placeholder="이름을 입력해주세요." required/>
          </div>
        </div>
        <div class="form-group row">
          <label for="id" class="col-sm-3 col-form-label">아이디&nbsp;<span style="color: red">*</span></label>
          <div class="col-sm-7">
            <input type="text" class="form-control" v-model="user.id" placeholder="아이디를 입력해주세요." required/>
          </div>
        </div>
        <button class="btn btn-dark d-flex m-auto" @submit.prevent="" @click="findPass">비밀번호 찾기</button>
      </form>
    </section>
  </div>
</template>

<script>
import http from "@/api/http-common";

export default {
  name: "UserFindPass",
  data() {
    return {
      user: {},
    }
  },
  methods: {
    findPass() {
      // 서버로 비동기 요청
      http.post(`/users/findpass`, this.user)
        .then(({ data }) => {
          console.log(data);
          if (data.message == 'success') {
            // 메일발송에 성공한 경우
            alert("메일이 발송되었습니다!!")
            this.$router.push({ name: "login" });
          } else if(data.message == 'no data'){
            // 등록된 유저가 아닌 경우
            alert("정보를 다시 입력해주세요!!");
          }
        })
    }
  }
}
</script>

<style>

</style>