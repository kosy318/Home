<template>
  <div
    class="row p-3 list"
    @click="getApt"
    @mouseenter="setInfo({ apt, flag: true })"
    @mouseleave="setInfo({ apt, flag: false })"
  >
    <!-- <div class="col-sm-2 my-auto">{{ index + 1 }}</div> -->
    <div class="col-sm-3 my-auto">
      <span>{{ apt.dealYear }}</span
      ><br />
      <span>{{ apt.dealMonth }}</span
      >.<span>{{ apt.dealDay }}</span>
    </div>
    <div class="col col-sm-9">
      <div class="row-sm-6 align-self-center">
        {{ apt.apartmentName }}
      </div>
      <div class="price row-sm-6 align-self-center">{{ apt.dealAmount }}만 원</div>
      <!-- <div id="detail" class="mt-3" v-show="showDetail">
        <div class="row">
          <div class="col-4">평수</div>
          <div class="col-8">{{ area }}평</div>
        </div>
        <div class="row">
          <div class="col-4">층</div>
          <div class="col-8">{{ apt.floor }}층</div>
        </div>
      </div> -->
      <div>
        <b-button v-b-modal.modal-1 class="btn-light" @click="setIndex(index), countUp(apt)"
          >자세히 보기</b-button
        >
      </div>
    </div>
  </div>
</template>

<script>
// import { mapActions } from "vuex";
import Constant from "@/Constant";
import { mapActions, mapMutations } from "vuex";

const aptStore = "AptStore";

export default {
  name: "AptListItem",
  data() {
    return {
      hide: true,
      showDetail: false,
      area: 0,
    };
  },
  props: {
    index: Number,
    apt: Object,
    show: Boolean,
  },
  methods: {
    ...mapActions(aptStore, [Constant.SET_INFO, Constant.COUNT_UP]),
    ...mapMutations(aptStore, [Constant.SET_INDEX]),
    getApt: function () {
      // get Lat, Lng
      console.log(this.apt);
      // this.showDetail = !this.showDetail;
      this.$store.dispatch(aptStore + "/" + Constant.GET_LAT_LNG, { apt: this.apt });
    },
    selectApt() {
      console.log("listRow : ", this.apt);
      this.detailApt(this.apt);
    },
    // showPrice(flag) {
    //   if (!this.show) this.hide = !flag;
    // },
  },
};
</script>

<style scoped>
.list {
  border-bottom: 0.5px solid white;
}
.list:hover {
  font-weight: bold;
  background-color: rgb(174, 189, 202, 0.8);
}
.mouse-over-hide {
  display: none;
}
.always-show {
  display: block;
}
.showDetail {
  font-weight: bold;
  background-color: #f5efe6;
}
</style>
<style scoped src="@/views/style/style.css"></style>
