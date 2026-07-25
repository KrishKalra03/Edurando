import { defineStore } from "pinia";

export const useEmailStore = defineStore('email', {
    state: () => ({ email: "" }),
    persist: {
        storage: sessionStorage,
    },
})
