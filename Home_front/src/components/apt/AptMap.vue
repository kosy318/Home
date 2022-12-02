<template>
  <div id="container">
    <div id="rvWrapper">
      <div id="roadview" style="width: 100%; height: 100%"></div>
      <!-- 로드뷰를 표시할 div 입니다 -->
      <div id="close" title="로드뷰닫기" @click="closeRoadview"><b-icon icon="x-lg"></b-icon></div>
    </div>
    <div id="mapWrapper">
      <div id="map" style="width: 100%; height: 100%"></div>
      <!-- 지도를 표시할 div 입니다 -->
      <ul id="category">
        <li id="MT1" data-order="0" @click="onClickCategory('MT1')" bac>
          <font-awesome-icon icon="fa-cart-shopping" style="width: 30px; height: 30px" />
          <br>
          마트
        </li>
        <li id="CS2" data-order="1" @click="onClickCategory('CS2')">
          <font-awesome-icon icon="fa-store" style="width: 30px; height: 30px" />
          <br>
          편의점
        </li>
        <li id="HP8" data-order="2" @click="onClickCategory('HP8')">
          <font-awesome-icon icon="fa-house-medical" style="width: 30px; height: 30px" />
          <br>
          병원
        </li>
        <li id="SC4" data-order="3" @click="onClickCategory('SC4')">
          <font-awesome-icon icon="fa-graduation-cap" style="width: 30px; height: 30px" />
          <br>
          학교
        </li>
        <li id="CE7" data-order="4" @click="onClickCategory('CE7')">
          <font-awesome-icon icon="fa-mug-saucer" style="width: 30px; height: 30px" />
          <br>
          카페
        </li>
        <li id="SW8" data-order="5" @click="onClickCategory('SW8')">
          <font-awesome-icon icon="fa-train-subway" style="width: 30px; height: 30px" />
          <br>
          지하철
        </li>
      </ul>
      <div id="roadviewControl" @click="setRoadviewRoad"></div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import axios from "axios";
import $ from "jquery";
import Constant from "@/Constant";

const aptStore = "AptStore";

export default {
  name: "AptMap",
  data() {
    return {
      map: null,
      marker: [],
      rvmarker: null,
      overlayOn: false,
      container: null,
      mapWrapper: null,
      btnRoadview: null,
      btnMap: null,
      rvContainer: null,
      mapContainer: null,
      rvClient: null,
      rv: null,
      placeOverlay: null,
      contentNode: null,
      markers: null,
      currCategory: null,
      ps: null,
      polygons: [],
      customOverlay: null,
      infowindow: null,
      openInfoWindow: null,
      position: null,
    };
  },
  computed: {
    ...mapState(aptStore, [
      "address",
      "latLngList",
      "latLng",
      "apts",
      "apt",
      "jsonfile",
      "sidoName",
      "infoWindow",
    ]),
  },
  watch: {
    latLngList() {
      console.log("changed latlnglist");
      this.marker.forEach((ele) => {
        ele.setMap(null);
      });
      this.marker = [];

      var geocoder = new kakao.maps.services.Geocoder();

      // 주소로 좌표를 검색합니다
      console.log(this.address);
      geocoder.addressSearch(this.address, (result, status) => {
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
          var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

          // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
          console.log("set Center");
          this.map.setCenter(coords);
          this.map.setLevel(5);
        }
      });

      var positions = [];
      console.log(this.latLngList);
      for (let i = 0; i < this.latLngList.length; i++) {
        // console.log(this.latLngList.length);
        if (this.latLngList[i].lng == "-") continue;

        positions.push({
          title: this.apts[i].apartmentName,
          latlng: new kakao.maps.LatLng(this.latLngList[i].lng, this.latLngList[i].lat),
        });
        // console.log("positions", positions);
      }

      var imageSrc = require("@/assets/location.png");
      for (var i = 0; i < positions.length; i++) {
        // 마커 이미지의 이미지 크기 입니다
        var imageSize = new kakao.maps.Size(35, 35);

        // 마커 이미지를 생성합니다
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

        // 마커를 생성합니다
        let aptMarker = new kakao.maps.Marker({
          map: this.map, // 마커를 표시할 지도
          position: positions[i].latlng, // 마커를 표시할 위치
          title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
          image: markerImage, // 마커 이미지
        });
        this.marker.push(aptMarker);
      }
    },
    latLng() {
      // this.marker.forEach((ele) => {
      //   ele.setMap(null);
      // });
      if (this.latLng.lng == "-") return;

      this.position = {
        title: this.apt.apartmentName,
        latlng: new kakao.maps.LatLng(this.latLng.lng, this.latLng.lat),
      };

      // 마커를 생성합니다
      // this.marker.push(
      //   new kakao.maps.Marker({
      //     map: this.map, // 마커를 표시할 지도
      //     position: position.latlng, // 마커를 표시할 위치
      //     title: position.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
      //     image: markerImage, // 마커 이미지
      //   })
      //   );
    },
    jsonfile() {
      console.log("display poly " + this.jsonfile);
      this.initMap();
      let displayArea = this.displayArea;

      let seoul = require(`@/json/${this.jsonfile}`);
      // console.log(seoul);
      let data = seoul.features;
      let coordinates = [];
      let name = "";

      $.each(data, function (index, val) {
        coordinates = val.geometry.coordinates[0][0];

        name = val.properties.adm_nm;

        displayArea(coordinates, name);
        // console.log(index + " " + coordinates);
      });
      var geocoder = new kakao.maps.services.Geocoder();

      // 주소로 좌표를 검색합니다
      console.log(this.sidoName);
      geocoder.addressSearch(this.sidoName, (result, status) => {
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
          var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

          // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
          console.log("set Center");
          this.map.setCenter(coords);
          this.map.setLevel(8);
          this.latLng.Lat = coords.getLat();
          this.latLng.lng = coords.getLng();
        }
      });
    },
    infoWindow() {
      this.position = {
        title: this.infoWindow.apt.apartmentName,
        latlng: new kakao.maps.LatLng(this.infoWindow.latLng.lng, this.infoWindow.latLng.lat),
      };

      console.log("infowindow");
      var imageSrc = require("@/assets/location.png");

      // 마커 이미지의 이미지 크기 입니다
      var imageSize = new kakao.maps.Size(35, 35);

      // 마커 이미지를 생성합니다
      var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
      let map = this.map;
      // 인포윈도우를 생성합니다
      var iwContent = `<div style="width: 100%; padding:5px;">${this.position.title}<br>`, // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
        iwPosition = this.position.latlng; //인포윈도우 표시 위치입니다
      let infowindow = new kakao.maps.InfoWindow({
        position: iwPosition,
        content: iwContent,
      });
      if (this.infoWindow.flag) {
        if (this.openInfoWindow != null) this.openInfoWindow.close();
        this.openInfoWindow = infowindow;
        // 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
        infowindow.open(
          map,
          new kakao.maps.Marker({
            map: map, // 마커를 표시할 지도
            position: this.position.latlng, // 마커를 표시할 위치
            title: this.position.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
            image: markerImage, // 마커 이미지
          })
        );
        // console.log(this.position.latlng);
        this.map.setCenter(this.position.latlng);
      } else {
        if (this.openInfoWindow != null) this.openInfoWindow.close();
      }
    },
  },
  methods: {
    initMap() {
      this.container = document.getElementById("container"); // 지도와 로드뷰를 감싸고 있는 div 입니다
      this.mapWrapper = document.getElementById("mapWrapper"); // 지도를 감싸고 있는 div 입니다
      this.btnRoadview = document.getElementById("btnRoadview"); // 지도 위의 로드뷰 버튼, 클릭하면 지도는 감춰지고 로드뷰가 보입니다
      this.btnMap = document.getElementById("btnMap"); // 로드뷰 위의 지도 버튼, 클릭하면 로드뷰는 감춰지고 지도가 보입니다
      this.rvContainer = document.getElementById("roadview"); // 로드뷰를 표시할 div 입니다
      this.mapContainer = document.getElementById("map"); // 지도를 표시할 div 입니다

      // 마커를 클릭했을 때 해당 장소의 상세정보를 보여줄 커스텀오버레이입니다
      this.placeOverlay = new kakao.maps.CustomOverlay({ zIndex: 1 });
      this.contentNode = document.createElement("div"); // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
      this.markers = []; // 마커를 담을 배열입니다
      this.currCategory = ""; // 현재 선택된 카테고리를 가지고 있을 변수입니다

      let placePosition = new kakao.maps.LatLng(37.5012767241426, 127.039600248343);

      const options = {
        center: placePosition,
        level: 6,
      };
      this.map = new kakao.maps.Map(this.mapContainer, options);
      this.customOverlay = new kakao.maps.CustomOverlay({});
      this.infowindow = new kakao.maps.InfoWindow({ removable: true });

      // 로드뷰 객체를 생성합니다
      this.rv = new kakao.maps.Roadview(this.rvContainer);

      // 좌표로부터 로드뷰 파노라마 ID를 가져올 로드뷰 클라이언트 객체를 생성합니다
      this.rvClient = new kakao.maps.RoadviewClient();

      let rv = this.rv;
      let map = this.map;
      let overlayOn = this.overlayOn;
      let rvmarker = this.rvmarker;
      // 로드뷰에 좌표가 바뀌었을 때 발생하는 이벤트를 등록합니다
      kakao.maps.event.addListener(rv, "position_changed", function () {
        // 현재 로드뷰의 위치 좌표를 얻어옵니다
        var rvPosition = rv.getPosition();

        // 지도의 중심을 현재 로드뷰의 위치로 설정합니다
        map.setCenter(rvPosition);

        // 지도 위에 로드뷰 도로 오버레이가 추가된 상태이면
        if (overlayOn) {
          // 마커의 위치를 현재 로드뷰의 위치로 설정합니다
          rvmarker.setPosition(rvPosition);
        }
      });

      // 장소 검색 객체를 생성합니다
      this.ps = new kakao.maps.services.Places(this.map);

      // 지도에 idle 이벤트를 등록합니다
      kakao.maps.event.addListener(this.map, "idle", this.searchPlaces);

      // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
      this.contentNode.className = "placeinfo_wrap";

      // 커스텀 오버레이의 컨텐츠 노드에 mousedown, touchstart 이벤트가 발생했을때
      // 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록합니다
      this.addEventHandle(this.contentNode, "mousedown", kakao.maps.event.preventMap);
      this.addEventHandle(this.contentNode, "touchstart", kakao.maps.event.preventMap);

      // 커스텀 오버레이 컨텐츠를 설정합니다
      this.placeOverlay.setContent(this.contentNode);

      // 마커 이미지를 생성합니다
      var markImage = new kakao.maps.MarkerImage(
        "https://t1.daumcdn.net/localimg/localimages/07/2018/pc/roadview_minimap_wk_2018.png",
        new kakao.maps.Size(26, 46),
        {
          // 스프라이트 이미지를 사용합니다.
          // 스프라이트 이미지 전체의 크기를 지정하고
          spriteSize: new kakao.maps.Size(1666, 168),
          // 사용하고 싶은 영역의 좌상단 좌표를 입력합니다.
          // background-position으로 지정하는 값이며 부호는 반대입니다.
          spriteOrigin: new kakao.maps.Point(705, 114),
          offset: new kakao.maps.Point(13, 46),
        }
      );

      // 드래그가 가능한 마커를 생성합니다
      this.rvmarker = new kakao.maps.Marker({
        image: markImage,
        position: placePosition,
        draggable: true,
      });

      rvmarker = this.rvmarker;
      let toggleRoadview = this.toggleRoadview;
      // 마커에 dragend 이벤트를 등록합니다.
      kakao.maps.event.addListener(rvmarker, "dragend", function () {
        // 현재 마커가 놓인 자리의 좌표입니다
        var position = rvmarker.getPosition();

        // 마커가 놓인 위치를 기준으로 로드뷰를 설정합니다
        toggleRoadview(position);
      });

      overlayOn = this.overlayOn;
      rvmarker = this.rvmarker;
      //지도에 클릭 이벤트를 등록합니다
      kakao.maps.event.addListener(this.map, "click", function (mouseEvent) {
        // 지도 위에 로드뷰 도로 오버레이가 추가된 상태가 아니면 클릭이벤트를 무시합니다
        if (!overlayOn) {
          return;
        }

        // 클릭한 위치의 좌표입니다
        var position = mouseEvent.latLng;

        // 마커를 클릭한 위치로 옮깁니다
        rvmarker.setPosition(position);

        // 클락한 위치를 기준으로 로드뷰를 설정합니다
        toggleRoadview(position);
      });
    },
    toggleRoadview(position) {
      let rv = this.rv;
      let toggleMapWrapper = this.toggleMapWrapper;
      this.rvClient.getNearestPanoId(position, 50, function (panoId) {
        // 파노라마 ID가 null 이면 로드뷰를 숨깁니다
        if (panoId === null) {
          toggleMapWrapper(true, position);
        } else {
          toggleMapWrapper(false, position);

          // panoId로 로드뷰를 설정합니다
          rv.setPanoId(panoId, position);
        }
      });
    },
    toggleMapWrapper(active, position) {
      if (active) {
        // 지도를 감싸고 있는 div의 너비가 100%가 되도록 class를 변경합니다
        this.container.className = "";

        // 지도의 크기가 변경되었기 때문에 relayout 함수를 호출합니다
        this.map.relayout();

        // 지도의 너비가 변경될 때 지도중심을 입력받은 위치(position)로 설정합니다
        this.map.setCenter(position);
      } else {
        // 지도만 보여지고 있는 상태이면 지도의 너비가 50%가 되도록 class를 변경하여
        // 로드뷰가 함께 표시되게 합니다
        if (this.container.className.indexOf("view_roadview") === -1) {
          this.container.className = "view_roadview";

          // 지도의 크기가 변경되었기 때문에 relayout 함수를 호출합니다
          this.map.relayout();

          // 지도의 너비가 변경될 때 지도중심을 입력받은 위치(position)로 설정합니다
          this.map.setCenter(position);
        }
      }
    },
    toggleOverlay(active) {
      if (active) {
        this.overlayOn = true;

        // 지도 위에 로드뷰 도로 오버레이를 추가합니다
        this.map.addOverlayMapTypeId(kakao.maps.MapTypeId.ROADVIEW);

        // 지도 위에 마커를 표시합니다
        this.rvmarker.setMap(this.map);

        // 마커의 위치를 지도 중심으로 설정합니다
        this.rvmarker.setPosition(this.map.getCenter());

        // 로드뷰의 위치를 지도 중심으로 설정합니다
        this.toggleRoadview(this.map.getCenter());
      } else {
        this.overlayOn = false;

        // 지도 위의 로드뷰 도로 오버레이를 제거합니다
        this.map.removeOverlayMapTypeId(kakao.maps.MapTypeId.ROADVIEW);

        // 지도 위의 마커를 제거합니다
        this.rvmarker.setMap(null);
      }
    },
    setRoadviewRoad() {
      var control = document.getElementById("roadviewControl");

      // 버튼이 눌린 상태가 아니면
      if (control.className.indexOf("active") === -1) {
        control.className = "active";

        // 로드뷰 도로 오버레이가 보이게 합니다
        this.toggleOverlay(true);
      } else {
        control.className = "";

        // 로드뷰 도로 오버레이를 제거합니다
        this.toggleOverlay(false);
      }
    },
    closeRoadview() {
      var position = this.rvmarker.getPosition();
      this.toggleMapWrapper(true, position);
    },
    searchPlaces() {
      if (!this.currCategory) {
        return;
      }

      // 커스텀 오버레이를 숨깁니다
      this.placeOverlay.setMap(null);

      // 지도에 표시되고 있는 마커를 제거합니다
      this.removeMarker();
      let lat = this.latLng.lat;
      let lng = this.latLng.lng;

      // console.log(lat, lng);

      let url = `https://dapi.kakao.com/v2/local/search/category.json?category_group_code=${this.currCategory}&page=1&size=15&sort=distance&x=${lat}&radius=10000&y=${lng}`;
      let key = process.env.VUE_APP_KAKAO_REST_API_KEY;
      axios
        .get(url, {
          headers: {
            Authorization: key,
          },
        })
        .then(({ data }) => {
          this.displayPlaces(data.documents);
        });
    },
    addEventHandle(target, type, callback) {
      if (target.addEventListener) {
        target.addEventListener(type, callback);
      } else {
        target.attachEvent("on" + type, callback);
      }
    },
    displayPlaces(places) {
      // 몇번째 카테고리가 선택되어 있는지 얻어옵니다
      // 이 순서는 스프라이트 이미지에서의 위치를 계산하는데 사용됩니다
      var order = document.getElementById(this.currCategory).getAttribute("data-order");
      for (var i = 0; i < places.length; i++) {
        // 마커를 생성하고 지도에 표시합니다
        var marker = this.addMarker(new kakao.maps.LatLng(places[i].y, places[i].x), order);

        // 마커와 검색결과 항목을 클릭 했을 때
        // 장소정보를 표출하도록 클릭 이벤트를 등록합니다
        this.addPlaceInfo(marker, places[i]);
      }
    },
    addPlaceInfo(marker, place) {
      let displayPlaceInfo = this.displayPlaceInfo;
      kakao.maps.event.addListener(marker, "click", function () {
        displayPlaceInfo(place);
      });
    },
    addMarker(position, order) {
      console.log(order);
      let imageSrc = require("@/assets/map-mark.png");
      // if (order == 0) {
      //   imageSrc = require("@/assets/marker.png");
      // } else if (order == 1) {
      //   imageSrc = require("@/assets/shop.png");
      // } else if (order == 2) {
      //   imageSrc = require("@/assets/hospital2.png");
      // } else if (order == 3) {
      //   imageSrc = require("@/assets/school.png");
      // } else if (order == 4) {
      //   imageSrc = require("@/assets/cafe.png");
      // } else if (order == 5) {
      //   imageSrc = require("@/assets/train-station.png");
      // }
      console.log(imageSrc);
      var imageSize = new kakao.maps.Size(35, 35); // 마커 이미지의 크기
      var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
      var marker = new kakao.maps.Marker({
        position: position, // 마커의 위치
        image: markerImage,
      });

      marker.setMap(this.map); // 지도 위에 마커를 표출합니다
      this.markers.push(marker); // 배열에 생성된 마커를 추가합니다
      return marker;
    }, // 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
    displayPlaceInfo(place) {
      var content =
        '<div class="placeinfo">' +
        '   <a class="title" href="' +
        place.place_url +
        '" target="_blank" title="' +
        place.place_name +
        '">' +
        place.place_name +
        "</a>";

      if (place.road_address_name) {
        content +=
          '    <span title="' +
          place.road_address_name +
          '">' +
          place.road_address_name +
          "</span>" +
          '  <span class="jibun" title="' +
          place.address_name +
          '">(지번 : ' +
          place.address_name +
          ")</span>";
      } else {
        content += '    <span title="' + place.address_name + '">' + place.address_name + "</span>";
      }

      content +=
        '    <span class="tel">' + place.phone + "</span>" + "</div>" + '<div class="after"></div>';

      this.contentNode.innerHTML = content;
      this.placeOverlay.setPosition(new kakao.maps.LatLng(place.y, place.x));
      this.placeOverlay.setMap(this.map);
    },
    onClickCategory(id) {
      var el = document.getElementById(id);
      var className = el.className;

      this.placeOverlay.setMap(null);

      if (className === "on") {
        this.currCategory = "";
        this.changeCategoryClass(className);
        this.removeMarker();
      } else {
        this.currCategory = id;
        this.changeCategoryClass(className);
        this.searchPlaces();
      }
    },
    changeCategoryClass(el) {
      var category = document.getElementById("category"),
        children = category.children,
        i;

      for (i = 0; i < children.length; i++) {
        children[i].className = "";
      }

      if (el.className) {
        el.className = "on";
      }
    },
    removeMarker() {
      for (var i = 0; i < this.markers.length; i++) {
        this.markers[i].setMap(null);
      }
      this.markers = [];
    },
    displayArea(coordinates, name) {
      // console.log(name);
      let polygonPath = []; // 폴리곤 그려줄 path

      $.each(coordinates, function (index, coordinate) {
        // console.log(coordinate[1], coordinate[0]);
        polygonPath.push(new kakao.maps.LatLng(coordinate[1], coordinate[0]));
      });

      // console.log(polygonPath);

      let map = this.map;
      var polygon = new kakao.maps.Polygon({
        map: map, // 다각형을 표시할 지도 객체
        path: polygonPath,
        strokeWeight: 2,
        strokeColor: "#7895B2",
        strokeOpacity: 1,
        fillColor: "#fff",
        fillOpacity: 0.3,
      });

      let customOverlay = this.customOverlay;

      // kakao.maps.event.addListener(polygon, "mouseover", function (mouseEvent) {
      //   // polygon.setOptions({ fillColor: "#AEBDCA", fillOpacity: 0.7 });
      //   customOverlay.setContent(
      //     '<div class="area" style="background-color: rgb(255, 255, 255, 0.7)">' + name + "</div>"
      //   );
      //   customOverlay.setPosition(mouseEvent.latLng);
      //   customOverlay.setMap(map);
      //   // setTimeout(function () {
      //   //   polygon.setOptions({ fillColor: "#fff", fillOpacity: 0.3 });
      //   //   customOverlay.setMap(null);
      //   // }, 700);
      // });

      // 다각형에 click 이벤트를 등록하고 이벤트가 발생하면 다각형의 이름과 면적을 인포윈도우에 표시합니다
      // let infowindow = this.infowindow;
      let self = this;
      kakao.maps.event.addListener(polygon, "click", function (mouseEvent) {
        polygon.setOptions({ fillColor: "#AEBDCA", fillOpacity: 0.7 });
        customOverlay.setContent(
          '<div class="area mb-5" style="background-color: rgb(255, 255, 255, 0.7); z-index=10000">' +
            name +
            "</div>"
        );
        customOverlay.setPosition(mouseEvent.latLng);
        customOverlay.setMap(map);
        setTimeout(function () {
          polygon.setOptions({ fillColor: "#fff", fillOpacity: 0.3 });
          customOverlay.setMap(null);
        }, 1000);

        // console.log(typeof polygonPath);

        self.$store.dispatch(aptStore + "/" + Constant.GET_APT_POLY, { name, polygonPath });
        // console.log("clicked", mouseEvent.latLng.getLat());
        // var lat = mouseEvent.latLng.getLat();
        // var lng = mouseEvent.latLng.getLng();
        self.$store.commit(aptStore + "/" + Constant.SET_POLY_ADDRESS, name);
      });
    },
  },
  mounted() {
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      // script 태그 객체 생성
      const script = document.createElement("script");
      // src 속성을 추가하며 .env.local에 등록한 service 키 활용
      const SERVICE_KEY = process.env.VUE_APP_KAKAO_MAP_API_KEY;
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${SERVICE_KEY}&libraries=services`;
      document.head.appendChild(script);

      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
    }
  },
};
</script>

<style scoped>
#container {
  overflow: hidden;
  height: 700px;
  position: relative;
}
#mapWrapper {
  width: 100%;
  height: 700px;
  z-index: 1;
}
#rvWrapper {
  width: 50%;
  height: 700px;
  top: 0;
  right: 0;
  position: absolute;
  z-index: 0;
}
#container.view_roadview #mapWrapper {
  width: 50%;
}
#roadviewControl {
  position: absolute;
  top: 5px;
  right: 5px;
  width: 42px;
  height: 42px;
  z-index: 1;
  cursor: pointer;
  background: url(https://t1.daumcdn.net/localimg/localimages/07/2018/pc/common/img_search.png) 0 -450px
    no-repeat;
}
#roadviewControl.active {
  background-position: 0 -350px;
}
#close {
  position: absolute;
  padding: 4px;
  top: 5px;
  left: 5px;
  cursor: pointer;
  background: #fff;
  border-radius: 4px;
  border: 1px solid #c8c8c8;
  box-shadow: 0px 1px #888;
}
#close .img {
  display: block;
  background: url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/rv_close.png) no-repeat;
  width: 14px;
  height: 14px;
}
#category {
  position: absolute;
  top: 10px;
  left: 10px;
  border-radius: 5px;
  border: 1px solid #909090;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.4);
  background: #fff;
  overflow: hidden;
  z-index: 2;
  padding: 0;
}
#category li {
  float: left;
  list-style: none;
  width: 50px;
  border-right: 1px solid #acacac;
  padding: 6px 0;
  text-align: center;
  cursor: pointer;
}
#category li.on {
  background: #eee;
}
#category li:hover {
  background: #ffe6e6;
  border-left: 1px solid #acacac;
  margin-left: -1px;
}
#category li:last-child {
  margin-right: 0;
  border-right: 0;
}

#category li span {
  display: block;
  margin: 0 auto 3px;
  width: 27px;
  height: 28px;
}
#category li .bank {
  background-position: -10px 0;
}
#category li .mart {
  background-position: -10px -36px;
}
#category li .pharmacy {
  background-position: -10px -72px;
}
#category li .oil {
  background-position: -10px -108px;
}
#category li .cafe {
  background-position: -10px -144px;
}
#category li .store {
  background-position: -10px -180px;
}
#category li.on .category_bg {
  background-position-x: -46px;
}
.placeinfo_wrap {
  position: absolute;
  bottom: 28px;
  left: -150px;
  width: 300px;
}
.placeinfo {
  position: relative;
  width: 100%;
  border-radius: 6px;
  border: 1px solid #ccc;
  border-bottom: 2px solid #ddd;
  padding-bottom: 10px;
  background: #fff;
}
.placeinfo:nth-of-type(n) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}
.placeinfo_wrap .after {
  content: "";
  position: relative;
  margin-left: -12px;
  left: 50%;
  width: 22px;
  height: 12px;
  background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png");
}
.placeinfo a,
.placeinfo a:hover,
.placeinfo a:active {
  color: #fff;
  text-decoration: none;
}
.placeinfo a,
.placeinfo span {
  display: block;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
.placeinfo span {
  margin: 5px 5px 0 5px;
  cursor: default;
  font-size: 13px;
}
.placeinfo .title {
  font-weight: bold;
  font-size: 14px;
  border-radius: 6px 6px 0 0;
  margin: -1px -1px 0 -1px;
  padding: 10px;
  color: #fff;
  background: #2c3e50;
  background: #2c3e50 url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/arrow_white.png)
    no-repeat right 14px center;
}
.placeinfo .tel {
  color: #0f7833;
}
.placeinfo .jibun {
  color: #999;
  font-size: 11px;
  margin-top: 0;
}
</style>
