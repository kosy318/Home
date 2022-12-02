<template>
  <tr @click="getNotice(notice.num)">
    <td>{{ notice.num }}</td>
    <td>{{ notice.title }}</td>
    <td>{{ notice.regdate }}</td>
    <td>{{ notice.count }}</td>
  </tr>
</template>

<script>
import Constant from "@/Constant";
import { mapActions } from "vuex";

const noticeStore = "NoticeStore";

export default {
  name: "NoticeListItem",
  props: {
    notice: {},
  },
  methods: {
    ...mapActions(noticeStore, [Constant.GET_NOTICE]),
  },
};
</script>

<style></style>
