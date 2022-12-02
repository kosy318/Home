<template>
  <div class="card mb-3">
    <div class="card-body p-3 apt">
      <div class="row">
        <div class="col-sm-2" style="font-weight: bold; font-size: 2em; color: #7895b2">
          {{ index + 1 }}
        </div>
        <div class="col-sm-10 row text-left">
          <div class="col">
            <h6 class="mb-0">
              <b>{{ apt.apartmentName }}</b>
            </h6>
            <p class="mb-0 text-body">{{ apt.dealYear }}-{{ apt.dealMonth }}-{{ apt.dealDay }}</p>
          </div>
          <div>
            <b-button v-b-modal.modal-2 class="btn-light" @click="setIndex(index), countUp(apt)"
              >자세히 보기</b-button
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapMutations } from "vuex";
import Constant from "@/Constant";

const aptStore = "AptStore";

export default {
  name: "AptTopFive",
  props: ["index", "apt"],
  methods: {
    ...mapActions(aptStore, [Constant.COUNT_UP]),
    ...mapMutations(aptStore, [Constant.SET_INDEX]),
  },
};
</script>

<style src="@/views/style/style.css" scoped></style>
