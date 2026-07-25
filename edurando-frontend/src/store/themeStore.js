import { defineStore } from 'pinia'

export const useThemeStore = defineStore('theme', {
  state: () => ({
    isDark: typeof window !== 'undefined'
      ? window.matchMedia('(prefers-color-scheme: dark)').matches
      : false
  }),
  persist: true,
  actions: {
    toggle() {
      this.isDark = !this.isDark
    }
  }
})
