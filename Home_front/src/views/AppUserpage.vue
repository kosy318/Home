<template>
  <div>
    <div class="page-header min-vh-40 relative">
      <div class="container absolute">
        <div class="row">
          <div class="col-md-7 text-center mx-auto">
            <h3><b-icon icon="person-fill"></b-icon> 마이 페이지</h3>
          </div>
        </div>
      </div>
    </div>
    <section class="card card-body blur shadow-blur mx-5 md-4 mt-n6 py-5">
      <user-info></user-info>
    </section>
  </div>
</template>

<script>
import UserInfo from "@/components/userpage/UserInfo.vue";

export default {
  name: "AppUserpage",
  components: {
    UserInfo,
  },
};
</script>

<style scoped>
h3 {
  font-weight: 600;
}
</style>
