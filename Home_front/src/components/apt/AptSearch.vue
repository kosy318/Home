<template>
  <div>
    <div>
      <div v-if="select">
        <h4>
          {{ sidoName }} {{ gugunName }} {{ eupmyeondongName.dongName }}&nbsp;
          <font-awesome-icon
            v-if="!isFavorite && userInfo"
            icon="fa-regular fa-star"
            style="color: darkorange"
            @click="addFavorite"
          />
          <font-awesome-icon
            v-if="isFavorite && userInfo"
            icon="fa-star"
            style="color: darkorange"
            @click="deleteFavorite"
          />
        </h4>
      </div>
    </div>
    <div class="col">
      <b-form-group>
        <b-form-radio-group
          id="radio-group-1"
          v-model="search"
          :options="options"
          name="radio-options"
        ></b-form-radio-group>
      </b-form-group>
    </div>
    <div v-if="search == '아파트명 검색'" class="d-flex">
      <section class="d-flex flex-wrap">
        <input
          class="form-control options shadow md-4 mt-2 search-item"
          style="z-index: 1"
          v-model="apartmentName"
          @keyup.13="searchName"
        />
        <div style="z-index: 1">
          <button class="btn shadow md-4 mt-2 search" type="button" @click="searchName">
            검색
          </button>
        </div>
      </section>
    </div>
    <div v-else class="d-flex">
      <section class="d-flex flex-wrap">
        <select
          id="select1"
          class="m-auto options form-control shadow md-4 search-item"
          style="z-index: 1"
          v-model="sidoName"
          @change="
            (select = false), (isFavorite = false), getGugunList(sidoName), makePoly(sidoName)
          "
        >
          <option hidden>시/도</option>
          <option v-for="(sido, index) in sidoList" :key="index">{{ sido }}</option>
        </select>
        <select
          id="select2"
          class="m-auto options form-control shadow md-4 search-item"
          style="z-index: 1"
          v-model="gugunName"
          @change="
            (select = false), (isFavorite = false), getEupmyeondongList({ sidoName, gugunName })
          "
        >
          <option hidden>구/군</option>
          <option v-for="(gugun, index) in gugunList" :key="index">{{ gugun }}</option>
        </select>
        <select
          id="select3"
          class="m-auto options form-control shadow md-4 search-item"
          style="z-index: 1"
          v-model="eupmyeondongName"
          @change="(select = false), (isFavorite = false)"
        >
          <option hidden>읍/면/동</option>
          <option
            v-for="(eupmyeondong, index) in eupmyeondongList"
            :key="index"
            :value="{ dongCode: eupmyeondong.dongCode, dongName: eupmyeondong.dongName }"
          >
            {{ eupmyeondong.dongName }}
          </option>
        </select>
        <div id="date" class="m-auto d-flex options">
          <select id="year" class="form-control shadow md-4 date" style="z-index: 1" v-model="year">
            <option hidden>년</option>
            <option v-for="(year, index) in yearList" :key="index">{{ year }}</option>
          </select>
          <select
            id="month"
            class="form-control shadow md-4 date"
            style="z-index: 1"
            v-model="month"
          >
            <option hidden>월</option>
            <option v-for="(month, index) in monthList" :key="index">{{ month }}</option>
          </select>
        </div>
        <div style="z-index: 1">
          <button class="btn shadow md-4 mt-2 search" type="button" @click="searchApt">검색</button>
          <button class="btn shadow md-4 mt-2 search ml-2" type="button" @click="reset">
            지역 초기화
          </button>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import Constant from "@/Constant";
import { mapState, mapActions, mapMutations } from "vuex";

const aptStore = "AptStore";
const userStore = "UserStore";

const INTERVAL = 1;

let now = new Date();
let curYear = now.getFullYear();
let curMonth = now.getMonth() + 1;
export default {
  name: "AptSearch",
  data() {
    return {
      sidoName: "시/도",
      gugunName: "구/군",
      eupmyeondongName: "읍/면/동",
      year: "",
      yearList: [],
      month: "",
      monthList: [],
      select: false,
      isFavorite: false,
      search: "주소 검색",
      apartmentName: null,
      options: ["주소 검색", "아파트명 검색"],
    };
  },
  computed: {
    ...mapState(aptStore, ["sidoList", "gugunList", "eupmyeondongList", "filter", "filterContent"]),
    ...mapState(userStore, ["favorites", "userInfo"]),
  },
  created() {
    this.$store.dispatch(aptStore + "/" + Constant.GET_SIDO_LIST);

    this.year = curYear;
    this.month = curMonth;
    for (let i = 0; i < INTERVAL; i++) {
      this.yearList.push(curYear - i);
    }
    if (curMonth - 12 * INTERVAL < 0) this.yearList.push(curYear - INTERVAL);

    if (this.$route.params.message == "ok") {
      this.sidoName = this.$route.params.sidoName;
      this.gugunName = this.$route.params.gugunName;
      this.eupmyeondongName = this.$route.params.eupmyeondongName;
      this.month = "월";
      console.log("created set params");
      this.searchApt();
    }
  },
  watch: {
    year: function () {
      this.monthList = ["월"];
      if (this.year == curYear) {
        for (let i = 1; i < curMonth; i++) this.monthList.push(i);
      } else if (curYear - this.year <= INTERVAL) {
        for (let i = curMonth; i <= 12; i++) this.monthList.push(i);
      } else {
        for (let i = 1; i <= 12; i++) this.monthList.push(i);
      }
      this.month = "월";
    },
    favorites: function () {
      this.setFavorite();
    },
  },
  methods: {
    ...mapActions(userStore, ["addUserFavorite", "deleteUserFavorite", "getUserFavorites"]),
    ...mapActions(aptStore, [
      "getGugunList",
      "getEupmyeondongList",
      "searchAptByName",
      "getAptAddressFilter",
      "getAptNameFilter",
    ]),
    ...mapActions(userStore, ["addUserFavorite", "deleteUserFavorite", "getUserFavorites"]),
    ...mapMutations(aptStore, ["makePoly", "setAptFilter"]),
    searchApt: async function () {
      console.log("in searchApt");
      console.log("address:", this.sidoName, this.gugunName, this.eupmyeondongName);
      new Promise(() => {
        this.$store.commit(aptStore + "/" + Constant.SET_ADDRESS, {
          sidoName: this.sidoName,
          gugunName: this.gugunName,
          eupmyeondongName: this.eupmyeondongName,
        });
      });
      // 필터적용 X
      if (!this.filter) {
        await this.$store.dispatch(aptStore + "/" + Constant.SEARCH_APT, {
          year: this.year,
          month: this.month,
        });
      }
      // 필터적용
      else {
        let date = {
          year: this.year,
          month: this.month,
        };
        console.log(date);
        this.setAptFilter(this.filter);
        this.getAptAddressFilter(date);
      }

      // favorites에서 관심지역 추가되어있는 곳인지 확인하기
      this.setFavorite();
      this.select = true;
    },
    searchName() {
      // 필터적용 X
      if (!this.filter) {
        this.searchAptByName(this.apartmentName);
      }
      // 필터적용
      else {
        this.setAptFilter(this.filter);
        this.getAptNameFilter(this.apartmentName);
      }
    },
    async searchFavorite(sidoName, gugunName, eupmyeondongName) {
      this.$store.commit(aptStore + "/" + Constant.SET_ADDRESS, {
        sidoName,
        gugunName,
        eupmyeondongName,
      });
      await this.$store.dispatch(aptStore + "/" + Constant.SEARCH_APT, {
        year: this.year,
        month: this.month,
      });
      this.setFavorite();
    },
    setFavorite() {
      // 로그인 상태이면(userInfo!=null)
      if (this.userInfo) {
        this.getUserFavorites();
        this.isFavorite = false;
        this.favorites.forEach((favorite) => {
          if (favorite.dongCode == this.eupmyeondongName.dongCode) {
            this.isFavorite = true;
          }
        });
      } else {
        // 로그인하지 않은 상태이면(userInfo==null)
        this.isFavorite = false;
      }
    },
    addFavorite() {
      this.addUserFavorite(this.eupmyeondongName.dongCode);
    },
    deleteFavorite() {
      this.deleteUserFavorite(this.eupmyeondongName.dongCode);
    },
    reset() {
      this.sidoName = "시/도";
      this.gugunName = "구/군";
      this.eupmyeondongName = "읍/면/동";
      this.year = curYear;
      this.month = "월";
    },
  },
};
</script>

<style scoped>
.search-item {
  font-weight: bold;
  width: 200px;
}
.search {
  background-color: #7895b2;
  color: black;
  font-weight: bold;
}
.date {
  font-weight: bold;
  width: 100px;
}
select {
  margin: 10px;
}

@media (max-width: 1200px) {
  section {
    flex-direction: column;
  }
  #date {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }
  .date {
    font-weight: bold;
    width: 100%;
    margin: 0;
  }
  #year {
    margin-right: 8px;
  }
  .options {
    width: 500px;
    margin: 4px;
  }
  .search {
    width: 500px;
  }
}
</style>
