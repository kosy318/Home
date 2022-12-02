<template>
  <div class="col">
    <div class="row d-flex align-items-center">
      <label class="col-2 text-right">가격 : {{ filterContent.price[0] }} ~ {{ filterContent.price[1] }}(백만원)</label>
      <VueSimpleRangeSlider
        class="col-10"
        v-model="filterContent.price"
        :min="12"
        :max="7150"
        :exponential="false"
        activeBarColor="#7e7e7e"
        barColor="#bebebe"
        :keepJustSignificantFigures="true"
      />
    </div>
    <div class="row d-flex align-items-center">
      <label class="col-2 text-right">전용면적 : {{ filterContent.area[0] }}평 ~ {{ filterContent.area[1] }}평</label>
      <VueSimpleRangeSlider
        class="col-4"
        v-model="filterContent.area"
        :min="3"
        :max="74"
        :exponential="false"
        activeBarColor="#7e7e7e"
        barColor="#bebebe"
        :keepJustSignificantFigures="true"
      />
      <label class="col-2">층 : {{ filterContent.floor[0] }}층 ~ {{ filterContent.floor[1] }}층</label>
      <VueSimpleRangeSlider
        class="col-4"
        v-model="filterContent.floor"
        :min="-1"
        :max="54"
        :exponential="false"
        activeBarColor="#7e7e7e"
        barColor="#bebebe"
        :keepJustSignificantFigures="true"
      />
    </div>
    <div class="row d-flex align-items-center flex-wrap">
      <label class="col-2 text-right">준공년도</label>
      <b-select name="buildYear" id="buildYear" class="col-2 ml-4" v-model="filterContent.selYear">
        <option v-for="(year, index) in filterContent.years" :key="index">{{ year }}</option>
      </b-select>
      <button class="btn shadow md-4 search ml-2" type="button" @click="resetFilterContent">
        필터 초기화
      </button>
    </div>
  </div>
</template>

<script>
import VueSimpleRangeSlider from "vue-simple-range-slider/vue2";
import { mapState, mapMutations } from "vuex";

import "vue-simple-range-slider/vue2/css";

const aptStore = "AptStore";

export default {
  name: "AptFilterSearch",
  components: {
    VueSimpleRangeSlider,
  },
  computed: {
    ...mapState(aptStore, ["filterContent"]),
  },
  watch: {
    filterContent: function() {
      console.log("change");
    }
  },
  methods: {
    ...mapMutations(aptStore,["resetFilterContent"]),
  }
};
</script>

<style scoped>
label {
  font-weight: bold;
  font-size: 16px;
}
.search {
  background-color: #7895b2;
  color: black;
  font-weight: bold;
}
</style>
