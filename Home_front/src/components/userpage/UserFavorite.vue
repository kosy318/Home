<template>
  <div>
    <h4>나의 관심지역</h4>
    <div class="card-parent mt-5" style="width: 100%; height: 50%; overflow-y: auto; overflow-x: hidden;">
      <div class="zoom-card-container" v-for="favorite, index in favorites" :key="index" @click="moveFavorite(favorite)">
        <div
          class="card card-color m-3 auto"
        >
        <div class="front front-background">
          <div class="card-body text-center">
            <p class="text-white" style="height: 30px; line-height: 45px; font-size: 18px; font-weight: bold;">
              {{favorite.sidoName}} {{favorite.gugunName}} {{favorite.dongName}}
            </p>
          </div>
        </div>
        </div>
      </div>
      <div v-if="!favorites || favorites.length == 0">
        <p>등록된 관심지역이 없습니다..ㅠ</p>
        <p>추가하러 가시겠습니까?</p>
      </div>
    </div>
    <div class="mt-3">
      <button class="btn btn-dark" @click="movePage">등록하러 가기</button>
    </div>
  </div>
</template>

<script>
import Constant from "@/Constant";
import { mapActions, mapState } from "vuex";
const userStore = "UserStore"

export default {
  name: "UserFavorite",
  data() {
    return {
      
    }
  },
  computed: {
    ...mapState(userStore, ["favorites"]),
  },
  methods: {
    ...mapActions(userStore, [Constant.GET_USER_FAVORITES]),
    movePage() {
      this.$router.push({name: "apt"})
    },
    moveFavorite(favorite) {
      this.$router.push({
        name: "aptsearch", params: {
          message: "ok",
          sidoName: favorite.sidoName, gugunName: favorite.gugunName,
          eupmyeondongName: { dongCode: favorite.dongCode, dongName: favorite.dongName }
        }
      })
    }
  },
  created() {
    this.getUserFavorites();
  },
};
</script>

<style scoped>
.card-parent{
  width: 100%;

}
.card-parent::-webkit-scrollbar {
  width: 10px;
}
.card-parent::-webkit-scrollbar-thumb {
  background-color: #f5efe6;
  border-radius: 10px;
}
.card-parent::-webkit-scrollbar-track {
  background-color: white;
  border-radius: 10px;
  box-shadow: inset 0px 0px 5px white;
}
.zoom-card-container{
  width: 50%;
  transition: 0.5s;
  float: left;
}
.card.card-color {
  background: linear-gradient(195deg, #aebdca, #7895b2) !important;
  opacity: 0.85;
}
.zoom-card-container:hover {
  transform: scale(1.1);
}
h4{
  font-weight: 600;
}
</style>
