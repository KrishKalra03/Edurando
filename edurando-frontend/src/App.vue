<script setup>
import { onMounted, watch } from 'vue'
import NavBar from "@/modules/NavBar/components/NavBar.vue"
import { useThemeStore } from "@/store/themeStore"
import '@fortawesome/fontawesome-free/css/all.css'

const theme = useThemeStore()

function applyTheme(dark) {
  document.documentElement.classList.toggle('dark', dark)
}

onMounted(() => applyTheme(theme.isDark))
watch(() => theme.isDark, applyTheme)
</script>

<template>
  <NavBar />
  <RouterView />
</template>
