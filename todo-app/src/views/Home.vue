<template>
  <div class="container">
    <div class="header">
      <div class="title">备忘录</div>
      <div class="cmd">
        <el-button icon="el-icon-plus" size="mini" @click="toAdd"></el-button>
      </div>
    </div>
    <div class="items">
      <div
        :class="['item',{'today':isToday(item.time),'tomorrow':isTomorrow(item.time),'nextTomorrow':isNextTomorrow(item.time),'outOfDate':isOutOfDate(item.time)}]"
        v-for="item in items"
        :key="item.id"
      >
        <div class="time" v-if="item.time">{{ formateDate(item.time) }}</div>
        <el-divider direction="vertical" v-if="item.time"></el-divider>
        <img
          v-if="item.moneyFlag===1||item.moneyFlag===2"
          class="money_png"
          :src="item.moneyFlag===1?money_png:money_out_png"
          alt
        />
        <div class="title" @click="toUpdate(item)">{{ item.title }}</div>
      </div>
    </div>

    <el-dialog :title="item.id ? '修改任务' : '新增任务'" :visible.sync="dialogVisible" width="80%">
      <el-form ref="form" :model="item" :rules="rules" label-width="3.5rem" size="mini">
        <el-form-item label="标题" prop="title">
          <el-input v-model="item.title"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input type="textarea" :rows="5" v-model="item.content"></el-input>
        </el-form-item>
        <el-form-item label="时间">
          <el-date-picker
            v-model="item.time"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="钱">
          <el-radio v-model="item.moneyFlag" :label="0">无关</el-radio>
          <el-radio v-model="item.moneyFlag" :label="1">收入</el-radio>
          <el-radio v-model="item.moneyFlag" :label="2">支出</el-radio>
          <!-- <el-switch
            v-model="item.moneyFlag"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ccc"
          ></el-switch>
          <br />
          <el-switch
            v-model="item.moneyTypeFlag"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="收入"
            inactive-text="支出"
          ></el-switch>-->
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="remove" v-if="item.id" type="danger" size="mini">删 除</el-button>
        <el-button
          type="primary"
          size="mini"
          @click="item.id ? update() : save()"
        >{{ item.id ? "修 改" : "新 增" }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
// @ is an alias to /src
import money_png from "../assets/money.png";
import money_out_png from "../assets/money_out.png";
export default {
  name: "Home",
  data() {
    return {
      money_png,
      money_out_png,
      items: [],
      rules: {
        title: [{ required: true, message: "请输入标题", trigger: "blur" }]
      },
      dialogVisible: true,
      item: {
        id: "",
        title: "",
        content: "",
        time: this.$moment().format("YYYY-MM-DD"),
        moneyFlag: 0
      }
    };
  },
  created() {
    this.listItems();
  },
  methods: {
    isRecent(dateStr) {
      let diff = this.$moment(dateStr, "YYYY-MM-DD").diff(
        this.$moment(),
        "days"
      );
      return diff < 2 && diff >= 0;
    },
    formateDate(dateStr) {
      //几天前|前天|昨天|今天|明天|后天|[2019-]9-3
      let now = this.$moment();
      if (
        this.$moment(dateStr, "YYYY-MM-DD")
          .add(2, "days")
          .isSame(now, "day")
      ) {
        return "前天";
      }
      if (
        this.$moment(dateStr, "YYYY-MM-DD")
          .add(1, "days")
          .isSame(now, "day")
      ) {
        return "昨天";
      }

      if (this.$moment(dateStr, "YYYY-MM-DD").isSame(now, "day")) {
        return "今天";
      }
      if (
        this.$moment(dateStr, "YYYY-MM-DD")
          .add(-1, "days")
          .isSame(now, "day")
      ) {
        return "明天";
      }
      if (
        this.$moment(dateStr, "YYYY-MM-DD")
          .add(-2, "days")
          .isSame(now, "day")
      ) {
        return "后天";
      }

      if (this.$moment(dateStr, "YYYY-MM-DD").isBefore(now)) {
        return this.$moment(dateStr, "YYYY-MM-DD").fromNow();
      }
      //这周
      if (this.$moment(dateStr, "YYYY-MM-DD").isSame(now, "week")) {
        return this.$moment(dateStr, "YYYY-MM-DD").format("ddd");
      }
      //下周
      if (
        this.$moment(dateStr, "YYYY-MM-DD")
          .add(-7, "day")
          .isSame(now, "week")
      ) {
        return "下" + this.$moment(dateStr, "YYYY-MM-DD").format("ddd");
      }
      //本月
      if (this.$moment(dateStr, "YYYY-MM-DD").isSame(now, "month")) {
        return this.$moment(dateStr, "YYYY-MM-DD").format("D日");
      }
      //本年
      if (this.$moment(dateStr, "YYYY-MM-DD").isSame(now, "year")) {
        return this.$moment(dateStr, "YYYY-MM-DD").format("M月D日");
      }
      return this.$moment(dateStr, "YYYY-MM-DD").format("YYYY年M月D日");
    },

    isToday(dateStr) {
      return this.$moment(dateStr, "YYYY-MM-DD").isSame(this.$moment(), "day");
    },
    isTomorrow(dateStr) {
      return this.$moment(dateStr, "YYYY-MM-DD")
        .add(-1, "days")
        .isSame(this.$moment(), "day");
    },
    isNextTomorrow(dateStr) {
      return this.$moment(dateStr, "YYYY-MM-DD")
        .add(-2, "days")
        .isSame(this.$moment(), "day");
    },
    isOutOfDate(dateStr) {
      return this.$moment(dateStr, "YYYY-MM-DD").isBefore(
        this.$moment(),
        "day"
      );
    },

    listItems() {
      let items = this.getItemsService();
      items.sort((x, y) => x.time - y.time);
      this.items = items;
    },
    toAdd() {
      this.item = {
        id: "",
        title: "",
        content: "",
        time: this.$moment().format("YYYY-MM-DD"),
        moneyFlag: 0
      };
      this.dialogVisible = true;
    },
    toUpdate(item) {
      this.item = { ...item };
      this.dialogVisible = true;
    },
    alertSuccess(message) {
      this.$message({
        type: "success",
        message,
        duration: 500
      });
    },

    async update() {
      await new Promise((resolve, reject) => {
        this.$refs["form"].validate(valid => {
          if (valid) {
            resolve();
          } else {
            reject();
          }
        });
      });
      this.updateService(this.item);
      this.alertSuccess("修改成功");
      this.listItems();
      this.dialogVisible = false;
    },
    async save() {
      await new Promise((resolve, reject) => {
        this.$refs["form"].validate(valid => {
          if (valid) {
            resolve();
          } else {
            reject();
          }
        });
      });
      this.saveService(this.item);
      this.alertSuccess("添加成功");
      this.listItems();
      this.dialogVisible = false;
    },
    remove() {
      this.deleteService(this.item.id);
      this.alertSuccess("删除成功");
      this.listItems();
      this.dialogVisible = false;
    },

    sort(items) {
      items.sort((x, y) => {
        if (x.time && y.time) {
          return x.time > y.time ? 1 : -1;
        } else if (x.time) {
          return 1;
        } else if (y.time) {
          return -1;
        }
        return 1;
      });
    },

    down(id) {
      this.downService(id);
      this.listItems();
    },
    up(id) {
      this.upService(id);
      this.listItems();
    },

    getItemsService() {
      let items = window.localStorage.getItem("items");
      return items ? JSON.parse(items) : [];
    },
    updateService(item) {
      let items = this.getItemsService();
      let index = items.findIndex(i => i.id === item.id);
      items.splice(index, 1, { ...item });
      this.sort(items);
      window.localStorage.setItem("items", JSON.stringify(items));
    },
    getMaxId() {
      let items = this.getItemsService();
      const maxId = items.map(x => x.id).reduce((x, y) => Math.max(x, y), 0);
      return maxId + 1;
    },
    saveService(item) {
      let items = this.getItemsService();
      items.unshift(Object.assign({}, item, { id: this.getMaxId() }));
      this.sort(items);
      window.localStorage.setItem("items", JSON.stringify(items));
    },
    deleteService(id) {
      let items = this.getItemsService().filter(x => x.id !== id);
      window.localStorage.setItem("items", JSON.stringify(items));
    }
  }
};
</script>
<style lang="scss">
.el-dialog__header {
  // padding: 10px 20px 10px;
  border-bottom: 1px solid #eee;
}
.el-dialog__footer {
  // padding: 10px 20px 10px;
  border-top: 1px solid #eee;
}
.el-dialog__body {
  padding: 10px 20px 0px;
}
</style>
<style lang="scss" scoped>
.el-date-editor.el-input,
.el-date-editor.el-input__inner {
  width: 100%;
}
.container {
  height: 100%;
  display: flex;
  flex-direction: column;
  .header {
    display: flex;
    align-items: center;
    padding: 0.5rem;
    border-bottom: 1px solid #eee;
    .title {
      flex: 1 0 0;
      width: 0;
    }
  }
  .items {
    flex: 1 0 0;
    height: 0;
    .item {
      display: flex;
      align-items: center;
      padding: 0.5rem 0.5rem;
      border-bottom: 1px solid #eee;
      &.today {
        background-color: #e6a23c;
        color: #fff;
      }
      &.tomorrow {
        background-color: #ebbe7c;
        color: #fff;
      }
      &.nextTomorrow {
        background-color: #ecdabf;
        color: #fff;
      }
      &.outOfDate {
        background: rgb(245, 108, 108);
        color: #fff;
      }
      .title {
        flex: 1 0 0;
        width: 0;
      }
      .money_png {
        height: 1rem;
        margin-right: 0.5rem;
      }
    }
  }
}
</style>
