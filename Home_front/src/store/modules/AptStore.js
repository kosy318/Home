import http from "@/api/http-common";
import Constant from "@/Constant";
// import router from "@/router";

const AptStore = {
  namespaced: true,
  state: {
    address: null,
    apts: [],
    apt: {
      apartmentName: "null",
      area: 0,
      price: 0,
      floor: 0,
      buildYear: 0,
    },
    sidoList: [],
    gugunList: [],
    eupmyeondongList: [],
    sidoName: null,
    gugunName: null,
    dongName: null,
    dongCode: null,
    latLngList: [],
    nameList: [],
    temp: [],
    latLng: {
      lat: null,
      lng: null,
    },
    loading: false,
    서울특별시: "seoul.json",
    부산광역시: "busan.json",
    인천광역시: "incheon.json",
    충청북도: "chungcheong_north.json",
    충청남도: "chungcheong_south.json",
    대전광역시: "daejeon.json",
    강원도: "gangwon.json",
    광주광역시: "gwangju.json",
    경기도: "gyeongi.json",
    경상북도: "gyeongsang_north.json",
    경상남도: "gyeongsang_south.json",
    제주특별자치도: "jeju.json",
    전라북도: "jeonla_north.json",
    전라남도: "jeonla_south.json",
    세종특별자치시: "sejong.json",
    울산광역시: "ulsan.json",
    대구광역시: "daegu.json",
    jsonfile: "",
    infoWindow: {
      apt: null,
      flag: false,
    },
    filter: true,
    filterContent: {
      price: [12, 7150],
      area: [3, 74],
      floor: [-1, 54],
      years: ["2년 이내", "2 ~ 4년", "4 ~ 10년", "10년 이후"],
      selYear: "2년 이내",
    },
    filters: {
      price: [],
      area: [],
      floor: [],
      year: [],
    },
    idx: 0,
    five: null,
  },
  getters: {},
  mutations: {
    [Constant.SET_INFO]: (state, payload) => {
      console.log(payload.apt.apartmentName);
      state.infoWindow = { apt: payload.apt, flag: payload.flag, latLng: state.temp[0] };
      state.temp = [];
    },
    [Constant.GET_SIDO_LIST]: (state, payload) => {
      state.gugunList = [];
      state.eupmyeondongList = [];
      state.sidoList = payload.sidoList;
    },
    [Constant.GET_GUGUN_LIST]: (state, payload) => {
      state.gugunList = payload.gugunList;
    },
    [Constant.GET_EUPMYEONDONG_LIST]: (state, payload) => {
      state.eupmyeondongList = payload.eupmyeondongList;
    },
    [Constant.SEARCH_APT]: (state, payload) => {
      console.log("search apt: ", payload);
      state.apts = payload.apts;
      state.latLngList = payload.latLngList;
      state.nameList = payload.nameList;
      console.log("end");
    },
    [Constant.RESET_LAT_LNG_LIST]: (state) => {
      console.log("reset lat lng list");
      state.latLngList = [];
    },
    [Constant.GET_LAT_LNG_LIST]: (state, payload) => {
      console.log("set lat lng list");
      console.log(payload);
      state.latLngList = payload.latLngList;
    },
    [Constant.GET_LAT_LNG]: (state, payload) => {
      state.latLng = {};
      state.aptName = payload.aptName;
      state.latLng.lat = payload.lat;
      state.latLng.lng = payload.lng;
    },
    [Constant.SET_ADDRESS]: (state, payload) => {
      state.sidoName = payload.sidoName;
      state.gugunName = payload.gugunName;
      state.dongName = payload.eupmyeondongName.dongName;
      state.dongCode = payload.eupmyeondongName.dongCode;

      state.address = state.sidoName + " " + state.gugunName + " " + state.dongName;
      console.log("address set : " + state.address);
    },
    [Constant.SET_POLY_ADDRESS]: (state, payload) => {
      state.sidoName = payload.split(" ")[0];
      state.gugunName = payload.split(" ")[1];
      state.eupmyeondongName = payload.split(" ")[2];
      state.address = payload;
      console.log("address set : " + state.address);
    },
    [Constant.SET_APT]: (state, apt) => {
      state.apt = apt;
    },
    [Constant.GET_APT]: (state, apt) => {
      state.apt = apt;
    },
    [Constant.SET_LOADING]: (state, flag) => {
      state.loading = flag;
    },
    [Constant.MAKE_POLY]: (state, sidoName) => {
      state.sidoName = sidoName;
      state.jsonfile = state[sidoName];
      console.log("make poly", state[sidoName]);
    },
    [Constant.GET_APT_FILTER_SEARCH]: (state, payload) => {
      state.apts = payload.apts;
      state.latLngList = payload.latLngList;
    },
    [Constant.SEARCH_APT_BY_NAME]: (state, payload) => {
      console.log(payload);
      state.apts = payload.apts;
      state.latLngList = payload.latLngList;
    },
    [Constant.FIND_NAMES]: (state, names) => {
      console.log("names : ", names);
      state.sidoName = names.sidoName;
      state.gugunName = names.gugunName;
      console.log(state.sidoName, state.gugunName);
    },
    [Constant.PUSH_TEMP]: (state, latlng) => {
      state.temp.push(latlng);
    },
    [Constant.SET_LAT_LNG_LIST]: (state) => {
      state.latLngList = state.temp;
      console.log("set latlnglist", state.latLngList);
      state.temp = [];
    },
    [Constant.SET_LAT_LNG]: (state) => {
      state.latLng = state.temp[0];
      state.temp = [];
    },
    [Constant.SET_SHOW_FILTER]: (state, showFilter) => {
      state.filter = showFilter;
    },
    [Constant.SET_APT_FILTER]: (state) => {
      let filter = state.filterContent;
      let years = [
        [0, 2],
        [2, 4],
        [4, 10],
        [10, 1000],
      ];
      let year;
      for (let i = 0; i < filter.years.length; i++) {
        if (filter.years[i] === filter.selYear) {
          year = years[i];
          break;
        }
      }
      let price1 = filter.price[0] * 100;
      let price2 = filter.price[1] * 100;
      let price = [price1, price2];
      let area = [];
      area[0] = filter.area[0] / 0.3;
      area[1] = filter.area[1] / 0.3;
      let toyear = new Date().getFullYear();
      let yearfilter = [toyear - year[1], toyear - year[0]];

      state.filters = {
        price,
        area,
        floor: filter.floor,
        year: yearfilter,
      };
    },
    [Constant.SEARCH_BY_COUNT]: (state, payload) => {
      state.five = payload;
    },
    [Constant.SET_INDEX]: (state, payload) => {
      state.idx = payload;
    },
    [Constant.RESET_FILTER_CONTENT]: (state) => {
      state.filterContent = {
        price: [12, 7150],
        area: [3, 74],
        floor: [-1, 54],
        years: ["2년 이내", "2 ~ 4년", "4 ~ 10년", "10년 이후"],
        selYear: "2년 이내",
      };
    },
  },
  actions: {
    getSidoList({ commit }) {
      http.get("/home").then((response) => {
        commit(Constant.GET_SIDO_LIST, { sidoList: response.data });
      });
    },
    getGugunList({ commit }, payload) {
      http.get("/home/" + payload).then((response) => {
        commit(Constant.GET_GUGUN_LIST, { gugunList: response.data });
      });
    },
    getEupmyeondongList({ commit }, payload) {
      http.get("/home/" + payload.sidoName + "/" + payload.gugunName).then((response) => {
        commit(Constant.GET_EUPMYEONDONG_LIST, { eupmyeondongList: response.data });
      });
    },
    searchApt({ state, commit }, payload) {
      commit(Constant.SET_LOADING, true);
      console.log("searching");
      if (payload.month == "월") payload.month = "null";
      http
        .get("/home/search/" + state.dongCode + "/" + payload.year + "/" + payload.month)
        .then((response) => {
          commit(Constant.SEARCH_APT, response.data);
          // })
          // .then(() => {
          //   commit(Constant.RESET_LAT_LNG_LIST);
          // })
          // .then(() => {
          //   console.log("get lat lng...");
          //   dispatch(Constant.GET_LAT_LNG_LIST);
          console.log("end");
        })
        .then(() => commit(Constant.SET_LOADING, false));
    },
    async findNames({ state, commit }, apt) {
      console.log("findNames:", apt);
      await http
        .get(`home/dongcode/${apt.dongCode}`)
        .then(({ data }) => {
          commit(Constant.FIND_NAMES, data);
        })
        .then(async () => {
          await http
            .get(
              "/home/getlatlng/" +
                state.sidoName +
                " " +
                state.gugunName +
                " " +
                apt.roadName +
                " " +
                apt.roadNameBuildingBonCode +
                "-" +
                apt.roadNameBuildingBuCode
            )
            .then(async (response) => {
              console.log("lat lng : ", response.data);
              await commit(Constant.PUSH_TEMP, response.data);
              console.log("lat lng temp : ", state.temp[0]);
            });
        });
    },
    getLatLngList({ state, dispatch }) {
      console.log("getLatLngList inside");
      state.apts.forEach(async (apt) => {
        await dispatch(Constant.FIND_NAMES, apt);
      });
    },
    async getLatLng({ commit, dispatch }, payload) {
      commit(Constant.SET_APT, payload.apt);
      await dispatch(Constant.FIND_NAMES, payload.apt);

      console.log("set latlng!!!----");
      commit(Constant.SET_LAT_LNG);
    },
    getAptPoly({ commit }, payload) {
      commit(Constant.SET_LOADING, true);
      http
        .post("/home/poly/" + payload.name, { body: payload.polygonPath })
        .then((response) => {
          console.log(response.data);
          commit(Constant.SEARCH_APT, { apts: response.data.apts });
          commit(Constant.GET_LAT_LNG_LIST, response.data);
          console.log("end");
        })
        .then(() => {
          commit(Constant.SET_LOADING, false);
        });
    },
    [Constant.GET_APT_ADDRESS_FILTER]: async ({ commit, state }, payload) => {
      console.log(state.filters);
      commit(Constant.SET_LOADING, true);
      if (payload.month == "월") payload.month = "null";
      console.log(payload);
      await http
        .post(`/home/search/address/filter`, {
          minPrice: state.filters.price[0],
          maxPrice: state.filters.price[1],
          minArea: state.filters.area[0],
          maxArea: state.filters.area[1],
          minFloor: state.filters.floor[0],
          maxFloor: state.filters.floor[1],
          minYear: state.filters.year[0],
          maxYear: state.filters.year[1],
          dongCode: state.dongCode,
          dealYear: payload.year,
          dealMonth: payload.month,
        })
        .then(({ data }) => {
          if (data.message == "success") {
            commit(Constant.GET_APT_FILTER_SEARCH, data);
            console.log("success");
          } else {
            commit(Constant.GET_APT_FILTER_SEARCH, data);
            console.log("no data");
          }
        });
      commit(Constant.SET_LOADING, false);
    },
    [Constant.GET_APT_NAME_FILTER]: async ({ commit, state }, apartmentName) => {
      console.log(state.filters);
      commit(Constant.SET_LOADING, true);
      await http
        .post(`/home/search/name/filter`, {
          minPrice: state.filters.price[0],
          maxPrice: state.filters.price[1],
          minArea: state.filters.area[0],
          maxArea: state.filters.area[1],
          minFloor: state.filters.floor[0],
          maxFloor: state.filters.floor[1],
          minYear: state.filters.year[0],
          maxYear: state.filters.year[1],
          apartmentName,
        })
        .then(({ data }) => {
          if (data.message == "success") {
            commit(Constant.GET_APT_FILTER_SEARCH, data);
            console.log("success");
          } else {
            console.log("no data");
          }
        })
        .catch();
      commit(Constant.SET_LOADING, false);
    },
    [Constant.SEARCH_APT_BY_NAME]: async ({ commit }, apartmentName) => {
      commit(Constant.SET_LOADING, true);
      await http.get(`/home/search/${apartmentName}`).then(({ data }) => {
        console.log(data);
        commit(Constant.SEARCH_APT_BY_NAME, data);
        commit(Constant.SET_LOADING, false);
      });
    },
    [Constant.SET_INFO]: async ({ commit, dispatch }, payload) => {
      await dispatch(Constant.FIND_NAMES, payload.apt);
      commit(Constant.SET_INFO, payload);
    },
    [Constant.COUNT_UP]: (_, payload) => {
      console.log(payload.num, " count up");
      http.put(`/home/countup`, payload);
    },
    [Constant.SEARCH_BY_COUNT]: ({ commit }) => {
      http.get("/home/count").then((response) => {
        console.log(response.data);
        commit(Constant.SEARCH_BY_COUNT, response.data);
      });
    },
  },
};

export default AptStore;
