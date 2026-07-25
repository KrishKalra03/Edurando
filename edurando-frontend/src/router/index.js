import { createRouter, createWebHistory } from 'vue-router'
import RegistrationPage from "@/modules/Registration/page/RegistrationPage.vue";
import Confirmation from "@/modules/Registration/page/Confirmation.vue";
import Home from "@/view/Home.vue";
import EditProfile from "@/modules/UserUpdate/components/EditProfile.vue";
import ChangePassword from "@/modules/UserUpdate/components/ChangePassword.vue";
import SubjectsTopic from "@/modules/UserUpdate/components/SubjectsTopic.vue";
import Login from "@/modules/Login/components/Login.vue";
import SubjectsTopicSave from "@/modules/UserUpdate/components/SubjectsTopicSave.vue";
import {useUserStore} from "@/store/store";
import {useEmailStore} from "@/modules/Registration/emailStore";
import {useEmailStore as useResetEmailStore} from "@/modules/ResetPassword/emailStore";
import NotFound from "@/view/NotFound.vue";
import Chat from "@/modules/Chat/Page/Chat.vue";
import Imprint from "@/modules/Imprint/imprint.vue";
import Search from "@/view/Search.vue";
import ResetPasswordPage from "@/modules/ResetPassword/page/ResetPasswordPage.vue";
import ConfirmationPage from "@/modules/ResetPassword/page/ConfirmationPage.vue";
import PasswordForm from "@/modules/ResetPassword/page/PasswordForm.vue";
import VerificationSuccess from "@/view/VerificationSuccess.vue";
import TermsOfService from "@/view/TermsOfService.vue";
import PrivacyPolicy from "@/view/PrivacyPolicy.vue";


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: Home
        },
        {
            path: '/page-not-found',
            component: NotFound
        },
        {
            path: '/:catchAll(.*)',
            redirect: '/page-not-found'
        },
        {
            path: '/register',
            name: 'register',
            component: RegistrationPage,
            meta: { guestOnly: true }
        },
        {
            path: '/confirm',
            name: 'confirm',
            component: Confirmation,
            meta: { needsEmail: true, guestOnly: true }
        },
        {
            path: '/editProfile',
            name: 'editProfile',
            component: EditProfile,
            meta: {
                needsAuth: true
            }
        },
        {
            path: '/changePassword',
            name: 'changePassword',
            component: ChangePassword,
            meta: {
                needsAuth: true
            }
        },
        {
            path: '/SubjectsTopics',
            name: 'SubjectsTopics',
            component: SubjectsTopic,
            meta: {
                needsAuth: true
            }
        },
        {
            path: '/login',
            name: 'login',
            component: Login,
            meta: { guestOnly: true }
        },
        {
            path: '/SubjectsTopicSave',
            name: 'SubjectsTopicSave',
            component: SubjectsTopicSave,
            meta: {
                needsAuth: true
            }
        },
        {
            path: '/imprint',
            name: 'imprint',
            component: Imprint
        },
        {
            path: '/chat/:id?',
            component: Chat,
            meta: {
                needsAuth: true
            }
        },
        {
            path: '/search',
            name: 'search',
            component: Search,
        },
        {
            path: '/reset-password',
            name: 'reset-password',
            component: ResetPasswordPage,
            meta: { guestOnly: true }
        },
        {
            path: '/confirmNumber',
            name: 'Confirmation-Number',
            component: ConfirmationPage,
            meta: { needsResetEmail: true, guestOnly: true }
        },
        {
            path: '/passwordform',
            name: 'Password-Form',
            component: PasswordForm,
            meta: { needsResetEmail: true, guestOnly: true }
        },
        {
            path: '/verify',
            name: 'verify',
            component: VerificationSuccess,
        },
        {
            path: '/TermsOfService',
            name: 'terms-of-service',
            component: TermsOfService,
        },
        {
            path: '/PrivacyPolicy',
            name: 'privacy-policy',
            component: PrivacyPolicy,
        },

    ]
})

router.beforeEach((to, from, next) => {
    const store          = useUserStore()
    const emailStore     = useEmailStore()
    const resetEmailStore = useResetEmailStore()
    const token          = localStorage.getItem('token')
    const isLoggedIn     = !store.isLoggedOut && !!token

    if (to.meta.guestOnly && isLoggedIn) {
        next('/')
    } else if (to.meta.needsAuth && !isLoggedIn) {
        next('/login')
    } else if (to.meta.needsEmail && !emailStore.email) {
        next('/register')
    } else if (to.meta.needsResetEmail && !resetEmailStore.email) {
        next('/reset-password')
    } else {
        next()
    }
})

export default router

