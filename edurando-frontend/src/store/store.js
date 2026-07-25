import { defineStore } from 'pinia';
import axios from 'axios';

export const useUserStore = defineStore('user', {
    state: () => ({
        user: null,
        token: null,
        isLoggedOut: true,
        chatReceiver: 0,
        chatReceivers: [],
        searchResult: []
    }),
    persist: true,
    getters: {
        getUser()         { return this.user },
        getIsLoggedOut()  { return this.isLoggedOut },
        getChatReceiver() { return this.chatReceiver },
        getChatReceivers(){ return this.chatReceivers },
        getSearchResult() { return this.searchResult },
    },
    actions: {
        setSession(token, user) {
            this.token       = token;
            this.user        = user;
            this.isLoggedOut = false;
            localStorage.setItem('token', token);
        },

        async fetchUser(email) {
            try {
                const response = await axios.get(`/profileByEmail/${email}`);
                this.user        = response.data;
                this.isLoggedOut = false;
            } catch (error) {
                console.error(error);
            }
        },

        async fetchUserById(id) {
            try {
                const response = await axios.get(`/profile/${id}`);
                this.user        = response.data;
                this.isLoggedOut = false;
            } catch (error) {
                console.error(error);
            }
        },

        async logOut() {
            try {
                await axios.post('/logout');
            } catch { /* ignore — token may already be expired */ }
            this.user        = null;
            this.token       = null;
            this.isLoggedOut = true;
            localStorage.removeItem('token');
        },

        async fetchChatReceiverById(id) {
            this.chatReceiver = id;
        },

        async fetchSearchResult(query) {
            try {
                const response = await axios.get('/profiles/search/' + query);
                this.searchResult = response.data;
            } catch (error) {
                console.error(error);
            }
        }
    },
});
