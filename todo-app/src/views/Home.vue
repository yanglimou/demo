<template>
  <div class="container">
    <div class="header">
      <div class="title">备忘录</div>
      <div class="cmd">
        <el-button
          icon="el-icon-plus"
          size="mini"
          :disabled="setting === 1"
          @click="toAdd"
        ></el-button>
        <el-button
          icon="el-icon-setting"
          size="mini"
          @click="setting = 1 - setting"
        ></el-button>
      </div>
    </div>
    <div class="items">
      <div class="item" v-for="(item, index) in items" :key="item.id">
        <div class="time">{{ item.time }}</div>
        <div class="title" @click="toUpdate(item)">{{ item.title }}</div>
        <el-button
          v-if="setting === 1"
          :disabled="index === items.length - 1"
          icon="el-icon-bottom"
          size="mini"
          @click="down(item.id)"
        ></el-button>
        <el-button
          v-if="setting === 1"
          :disabled="index === 0"
          icon="el-icon-top"
          size="mini"
          @click="up(item.id)"
        ></el-button>
      </div>
    </div>

    <el-dialog
      :title="item.id ? '修改任务' : '新增任务'"
      :visible.sync="dialogVisible"
      width="80%"
    >
      <el-form
        ref="form"
        :model="item"
        :rules="rules"
        label-width="3.5rem"
        size="mini"
      >
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
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="remove" v-if="item.id" type="danger" size="mini"
          >删 除</el-button
        >
        <el-button
          type="primary"
          size="mini"
          @click="item.id ? update() : save()"
          >{{ item.id ? "修 改" : "新 增" }}</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
// @ is an alias to /src

export default {
  name: "Home",
  data() {
    return {
      items: [],
      rules: {
        title: [{ required: true, message: "请输入标题", trigger: "blur" }],
      },
      dialogVisible: false,
      item: {
        id: "",
        title: "",
        content: "",
        time: "",
      },
      setting: 1,
    };
  },
  created() {
    this.listItems();
  },
  methods: {
    listItems() {
      this.items = this.getItemsService();
    },
    toAdd() {
      this.item = {
        id: "",
        title: "",
        content: "",
        time: "",
      };
      this.dialogVisible = true;
    },
    toSetting() {},
    toUpdate(item) {
      if (this.setting === 1) return;
      this.item = { ...item };
      this.dialogVisible = true;
    },
    alertSuccess(message) {
      this.$message({
        type: "success",
        message,
        duration: 500,
      });
    },

    async update() {
      await new Promise((resolve, reject) => {
        this.$refs["form"].validate((valid) => {
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
        this.$refs["form"].validate((valid) => {
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
      let index = items.findIndex((i) => i.id === item.id);
      items.splice(index, 1, { ...item });
      window.localStorage.setItem("items", JSON.stringify(items));
    },
    getMaxId() {
      let items = this.getItemsService();
      const maxId = items.map((x) => x.id).reduce((x, y) => Math.max(x, y), 0);
      return maxId + 1;
    },
    saveService(item) {
      let items = this.getItemsService();
      items.push(Object.assign({}, item, { id: this.getMaxId() }));
      window.localStorage.setItem("items", JSON.stringify(items));
    },
    deleteService(id) {
      let items = this.getItemsService().filter((x) => x.id !== id);
      window.localStorage.setItem("items", JSON.stringify(items));
    },
    downService(id) {
      let items = this.getItemsService();
      let index = items.findIndex((i) => i.id === id);
      let item = items.find((i) => i.id === id);
      items.splice(index, 1);
      items.splice(index + 1, 0, item);
      window.localStorage.setItem("items", JSON.stringify(items));
    },
    upService(id) {
      let items = this.getItemsService();
      let index = items.findIndex((i) => i.id === id);
      let item = items.find((i) => i.id === id);
      items.splice(index, 1);
      items.splice(index - 1, 0, item);
      window.localStorage.setItem("items", JSON.stringify(items));
    },
  },
};
</script>
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
    padding: 0 0.5rem;
    .item {
      display: flex;
      align-items: center;
      padding: 0.5rem 0;
      border-bottom: 1px solid #eee;
      .title {
        flex: 1 0 0;
        width: 0;
      }
    }
  }
}
</style>
