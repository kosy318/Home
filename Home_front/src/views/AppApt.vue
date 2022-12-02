<template>
  <div>
    <div class="page-header min-vh-40 relative">
      <div class="container absolute">
        <div class="row">
          <div class="col-md-7 text-center mx-auto">
            <h3><b-icon icon="house"></b-icon> House Service</h3>
          </div>
        </div>
      </div>
    </div>
    <section class="card card-body blur shadow-blur mx-5 md-4 mt-n6 py-5">
      <apt-search class="m-auto" />
      <button class="btn btn-light mt-4" v-if="!filter" @click="setShowFilter(true)">필터 검색</button>
      <apt-filter-search class="mt-4" v-if="filter" />
      <button class="btn btn-light mt-4" v-if="filter" @click="setShowFilter(false)">필터 닫기</button>
      <div class="bv-example-row pt-5 text-center">
        <b-row class="mt-2">
          <b-col cols="8">
            <apt-map />
          </b-col>
          <b-col cols="4 text-left">
            <!-- <div class="d-flex justify-content-end">
              <a class="btn" style="font-size: 0.8em" @click="showPrices()"><span v-if="!show">▼가격 펼치기</span><span v-else>▲가격 접기</span></a>
            </div> -->
            <apt-list />
          </b-col>
        </b-row>
      </div>
    </section>
  </div>
</template>

<script>
import AptSearch from "@/components/apt/AptSearch.vue";
import AptList from "@/components/apt/AptList";
import AptMap from "@/components/apt/AptMap.vue";
import AptFilterSearch from "@/components/apt/AptFilterSearch";
import { mapMutations, mapState } from "vuex";
// import AptDetail from "@/components/apt/AptDetail.vue";

const aptStore = "AptStore";

export default {
  name: "AppApt",
  components: {
    AptSearch,
    AptList,
    AptMap,
    AptFilterSearch,
  },
  data() {
    return {
      show: false,
    };
  },
  computed: {
    ...mapState(aptStore,["filter"]),
  },
  created() {
    window.scroll({
      top: 0,
      behavior: "smooth",
    });
  },
  methods: {
    ...mapMutations(aptStore,["setShowFilter"]),
    showPrices() {
      this.show = !this.show;
    },
  },
};
</script>

<style src="@/views/style/style.css"></style>
