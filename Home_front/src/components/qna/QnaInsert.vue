<template>
  <div class="container text-center pt-5">
    <h4>질문 추가</h4>
    <div>
      <div class="input-group mb-3">
        <div class="input-group-prepend">
          <span class="input-group-text" id="title">제목</span>
        </div>
        <input type="text" class="form-control" v-model="title" autofocus />
      </div>

      <div class="input-group">
        <div class="input-group-prepend">
          <span class="input-group-text">내용</span>
        </div>
        <textarea
          class="form-control"
          rows="10"
          aria-label="With textarea"
          v-model="content"
        ></textarea>
      </div>
    </div>
    <div class="mt-3">
      <div class="custom-control custom-radio custom-control-inline">
        <input
          type="radio"
          id="customRadioInline1"
          name="customRadioInline1"
          class="custom-control-input"
          value="N"
          v-model="secret"
          checked
        />
        <label class="custom-control-label" for="customRadioInline1">공개</label>
      </div>
      <div class="custom-control custom-radio custom-control-inline">
        <input
          type="radio"
          id="customRadioInline2"
          name="customRadioInline1"
          class="custom-control-input"
          value="Y"
          v-model="secret"
        />
        <label class="custom-control-label" for="customRadioInline2">비공개</label>
      </div>
    </div>
    <b-check class="mt-3" checked="true" v-model="sendMail">이메일 알람 받기</b-check>
    <br />
    <button type="submit" class="btn btn-dark mr-3" @click="setQna">등록</button>
    <router-link class="btn btn-dark" to="/qna/list">QnA 목록</router-link>

    <br />
  </div>
</template>

<script>
import Constant from "@/Constant";
import { mapActions, mapState } from "vuex";

const qnaStore = "QnaStore";
const userStore = "UserStore";

export default {
  name: "QnaInsert",
  data() {
    return {
      title: "",
      content: "",
      secret: "N",
      sendMail: true,
    };
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
  },
  methods: {
    ...mapActions(qnaStore, [Constant.ADD_QNA]),
    setQna() {
      let email;
      if (this.sendMail) {
        email = "Y";
      } else {
        email = "N";
      }
      let qna = {
        id: this.userInfo.id,
        secret: this.secret,
        title: this.title,
        content: this.content,
        email,
      };
      this.addQna(qna);
    },
  },
};
</script>

<style scoped>
h4 {
  font-weight: 600;
}
</style>
