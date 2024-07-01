<template>
  <div>
    <el-menu
        style="width: 200px; min-height: calc(100vh - 50px);"
        :default-active="path"
        router
        background-color="#30333c"
        text-color="#fff"
    >
      <el-menu-item index="/book">
        <svg class="icon" aria-hidden="true">
          <use xlink:href="#iconbook"></use>
        </svg>
        <span>书籍管理</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/userStore.js';

const user = ref({})
const route = useRoute()
const path = ref(route.path)
const userStore = useUserStore();

onMounted(() => {
  user.value = userStore.userInfo
})

</script>

<style scoped>
.icon {
  width: 30px;
  height: 30px;
  padding-top: 5px;
  padding-right: 10px;
}
</style>
