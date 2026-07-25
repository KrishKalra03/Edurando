import { defineStore } from "pinia";

export const useEmailStore = defineStore('reset-email', {
    state: () => ({ email: "" }),
    persist: {
        storage: sessionStorage,
    },
})
