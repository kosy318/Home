<template>
  <div style="z-index: -1">
    <div class="page-header min-vh-75 relative">
      <div class="container absolute">
        <div class="row">
          <div class="col-md-7 text-center mx-auto">
            <h1 class="logo text-black pt-3 mt-5" style="font-size: 10em">Home</h1>
            <p class="lead text-black mt-3" style="color: #7895b2">Find your home!</p>
          </div>
        </div>
      </div>
    </div>
    <section class="card card-body blur shadow-blur mx-5 md-4 mt-n6 py-5">
      <!-- 사이트 주요 기능 설명 -->
      <section class="my-5">
        <div class="container">
          <div class="row align-items-center">
            <div class="col-sm-4 ms-auto me-auto">
              <div class="rotating-card-container">
                <div
                  class="card card-rotate card-background card-background-mask-primary shadow-primary"
                >
                  <div class="front front-background" style="height: 400px; width: 100%">
                    <div class="card-body text-center">
                      <h3 class="text-white">
                        아파트 매매 <br />
                        실거래 자료
                      </h3>
                      <p class="text-white opacity-8">저희 사이트에서 손쉽게 확인할 수 있습니다</p>
                    </div>
                  </div>
                  <div class="back back-background">
                    <div class="card-body text-center">
                      <h3 class="text-white">Discover More</h3>
                      <p class="text-white opacity-8">
                        다양한 검색 방법을 통해 아파트 매매 실거래 자료를 확인할 수 있습니다.
                      </p>
                      <button class="btn btn-light btn-sm w-75 mx-auto mt-3">
                        <router-link id="apt" to="/apt" class="ml-3 mr-3 nav-link text-secondary"
                          >Try Search!</router-link
                        >
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-sm-8 ms-auto">
              <div class="row justify-content-start">
                <div class="col-sm-6">
                  <div class="info">
                    <b-icon icon="clipboard-data" style="color: #7895b2" font-scale="2"></b-icon>
                    <h5 class="font-weight-bolder mt-3">실거래 데이터</h5>
                    <p class="pe-5">
                      공공데이터 사이트에서 직접 가져온 아파트 매매 실거래 자료를 확인할 수
                      있습니다.
                    </p>
                  </div>
                </div>
                <div class="col-sm-6">
                  <div class="info">
                    <b-icon icon="search" style="color: #7895b2" font-scale="2"></b-icon>
                    <h5 class="font-weight-bolder mt-3">매매 자료 조회</h5>
                    <p class="pe-3">
                      시/도를 선택하여 나타난 다각형을 클릭하거나, 구/군, 읍/면/동을 검색하여 해당
                      장소의 데이터를 조회할 수 있습니다.
                    </p>
                  </div>
                </div>
              </div>
              <div class="row justify-content-start mt-5">
                <div class="col-sm-6 mt-3">
                  <b-iconstack font-scale="2" stackvariant="white">
                    <b-icon stacked icon="search" style="color: #7895b2"></b-icon>
                    <b-icon
                      stacked
                      icon="bar-chart-fill"
                      style="color: #7895b2"
                      scale="0.5"
                      shift-v="2"
                      shift-h="-1.4"
                    ></b-icon>
                  </b-iconstack>
                  <h5 class="font-weight-bolder mt-3">자세한 조회</h5>
                  <p class="pe-5">
                    지역 뿐만아니라 아파트 명을 검색하여 전국 조회할 수 있습니다. 또한 면적, 층,
                    가격의 범위를 정하여 세밀한 조회가 가능합니다.
                  </p>
                </div>
                <div class="col-sm-6 mt-3">
                  <div class="info">
                    <b-icon icon="card-list" scale="2" style="color: #7895b2"></b-icon>
                    <h5 class="font-weight-bolder mt-3">안내</h5>
                    <p class="pe-3">
                      공지사항과 QnA 게시판을 통해 안내를 도와드리고 있습니다. 급히 도움이
                      필요하시다면 챗봇도 사용해 보세요!
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Notice & 전 월 top 3 매물 -->
      <section>
        <div class="row">
          <div class="col-sm-7">
            <h4 class="mt-5">Blog</h4>
            <crawling-blog></crawling-blog>
            <h4 class="mt-5">News</h4>
            <crawling-news></crawling-news>
          </div>
          <div class="col-sm-5">
            <h4 class="mt-5">
              아파트 조회순 <span style="color: red; font-size: 0.5em; font-weight: bold">Hot</span>
            </h4>
            <div class="mt-3">
              <div v-for="(f, index) in five.apts" :key="f.num">
                <apt-top-five :index="index" :apt="f"></apt-top-five>
              </div>
              <b-modal id="modal-2" centered :title="five.apts[idx].apartmentName" ok-only>
                <b-alert show variant="secondary">
                  {{ five.nameList[idx].sidoName }} {{ five.nameList[idx].gugunName }}
                  {{ five.apts[idx].roadName }}
                  {{
                    getRoadNum(
                      five.apts[idx].roadNameBuildingBonCode,
                      five.apts[idx].roadNameBuildingBuCode
                    )
                  }}
                </b-alert>
                <div class="row">
                  <b class="col-4">거래 날짜</b>
                  <div class="col-8">
                    {{ five.apts[idx].dealYear }}-{{ five.apts[idx].dealMonth }}-{{
                      five.apts[idx].dealDay
                    }}
                  </div>
                </div>
                <div class="row">
                  <b class="col-4">거래 가격</b>
                  <div class="col-8 price">{{ five.apts[idx].dealAmount }}만 원</div>
                </div>
                <div class="row">
                  <div class="col-4"><b>건축년도</b></div>
                  <div class="col-8">{{ five.apts[idx].buildYear }}년</div>
                </div>
                <div class="row">
                  <div class="col-4"><b>평수</b></div>
                  <div class="col-8">{{ five.apts[idx].area * 1 * 0.3025 }}평</div>
                </div>
                <div class="row">
                  <div class="col-4"><b>층</b></div>
                  <div class="col-8">{{ five.apts[idx].floor }}층</div>
                </div>
              </b-modal>
            </div>
            <h4 class="mt-5">Notice</h4>
            <div class="mt-3">
              <div v-for="t in three" :key="t.num" @click="getNotice(t.num)">
                <div class="card justify-content-center mb-3">
                  <div class="card-body p-3 notice">
                    <h6 class="mb-0">
                      <b>{{ t.title }}</b>
                    </h6>
                    <p class="mb-0 text-body">{{ t.regdate }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </section>
  </div>
</template>

<script>
import CrawlingBlog from "@/components/crawling/CrawlingBlog.vue";
import CrawlingNews from "@/components/crawling/CrawlingNews.vue";
import AptTopFive from "@/components/apt/AptTopFive";
import { mapState, mapActions } from "vuex";
import Constant from "@/Constant";

const noticeStore = "NoticeStore";
const aptStore = "AptStore";

export default {
  name: "AppMain",
  created() {
    this.$store.dispatch("NoticeStore/" + Constant.SELECT_THREE);
    this.$store.dispatch("AptStore/" + Constant.SEARCH_BY_COUNT);
    window.scroll({
      top: 0,
      behavior: "smooth",
    });
  },
  data() {
    return {
      staticMap: null,
      map: null,
    };
  },
  components: {
    CrawlingBlog,
    CrawlingNews,
    AptTopFive,
  },
  computed: {
    ...mapState(noticeStore, ["three"]),
    ...mapState(aptStore, ["five", "idx"]),
  },
  methods: {
    ...mapActions(noticeStore, ["getNotice"]),
    getRoadNum(bon, bu) {
      let res = parseInt(bon);
      if (parseInt(bu) != 0) res += "-" + parseInt(bu);
      return res;
    },
  },
};
</script>

<style scoped src="./style/style.css"></style>
<style scoped>
.notice:hover {
  cursor: pointer;
  background-color: #f5efe6;
}
.apt:hover {
  cursor: pointer;
  background-color: #d3dde6;
}
</style>
