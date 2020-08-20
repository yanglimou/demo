import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import moment from "moment";
import "moment/locale/zh-cn";
Vue.use(ElementUI);
Vue.config.productionTip = false;
Vue.prototype.$moment = moment;
new Vue({
  router,
  render: (h) => h(App),
}).$mount("#app");
