<template>
  <div>
    <v-card class="ma-10 pa-5">
      <v-card-title>
        Category List
        <v-spacer></v-spacer>

        <v-text-field
          v-model="search"
          append-icon="mdi-magnify"
          label="Search By Name"
          single-line
          hide-details
          clearable
        ></v-text-field>
        <v-btn color="primary" @click="onClickCreateBtn()" class="ml-2 mt-2">
          <v-icon left> mdi-plus </v-icon>
          Add
        </v-btn>
      </v-card-title>
      <v-data-table
        :headers="headers"
        :items="categorylist"
        :items-per-page="10"
        :search="search"
      >
        <template v-slot:item.actions="{ item }">
          <!-- Edit Category -->
          <v-btn
            class="mr-3"
            color="primary"
            fab
            x-small
            dark
            @click="onClickUpdateBtn(item)"
          >
            <v-icon>mdi-pencil</v-icon>
          </v-btn>

          <!-- Delete Category -->
          <v-btn color="red" fab x-small dark @click="onClickDeleteBtn(item)">
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </template>
      </v-data-table>
    </v-card>
    <v-dialog v-model="createDialog" width="500">
      <v-card>
        <v-card-title>Create Category</v-card-title>
        <v-card-text>
          <v-form ref="createCategoryForm" v-model="createCategoryForm">
            <v-text-field
              v-model="name"
              label="Name"
              placeholder="Enter category name"
              required
              :rules="[(v) => !!v || 'Required']"
            >
            </v-text-field>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="createDialog = false">Cancel</v-btn>
          <v-btn color="primary" dark @click="createCategory()">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="updateDialog" width="500">
      <v-card>
        <v-card-title>Update Category</v-card-title>
        <v-card-text>
          <v-form ref="updateCategoryForm" v-model="updateCategoryForm">
            <v-text-field
              v-model="name"
              label="Name"
              placeholder="Enter category name"
              required
              :rules="[(v) => !!v || 'Required']"
            >
            </v-text-field>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="updateDialog = false">Cancel</v-btn>
          <v-btn color="primary" dark @click="updateCategory(item)">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="deleteDialog" width="500">
      <v-card>
        <v-card-title>Delete Category</v-card-title>
        <v-card-text> Are you sure to delete this? </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="deleteDialog = false">Cancel</v-btn>
          <v-btn color="red" dark @click="deleteCategory(selectItem.id)"
            >Delete</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import http from "@/utils/http";
// import category_form from "@/components/category_form.vue";

export default {
  name: "admin_category_list",
  // components: { category_form },

  data: () => {
    return {
      headers: [
        { text: "ID", value: "id", sortable: true },
        { text: "Name", value: "name", sortable: true },
        { text: "Actions", value: "actions", sortable: false },
      ],
      search: null,
      categorylist: [],
      selectItem: {},

      createDialog: false,
      updateDialog: false,
      deleteDialog: false,

      createCategoryForm: false,
      updateCategoryForm: false,

      toUpdateId: 0,
      name: "",
    };
  },
  async created() {
    this.fetchCategories({ params: { search: this.search } });
  },

  methods: {
    async fetchCategories(params) {
      const res = await http.get("/api/admin/categories", params);
      if (res && res.status === 200) {
        const data = await res.json();
        if (data) {
          this.categorylist = data;
        }
      }
    },

    async createCategory() {
      if (this.$refs.createCategoryForm.validate()) {
        const res = await http.post("/api/admin/categories/create", {
          name: this.name,
        });
        
        if (res && res.status === 200) {
          await this.fetchCategories();
          this.createDialog = false;
          this.name = "";
        }
      }
    },

    async updateCategory() {
      if (this.$refs.updateCategoryForm.validate()) {
        const res = await http.put(
          `/api/admin/categories/update/${this.toUpdateId}`,
          {
            name: this.name,
            id: this.toUpdateId,
          }
        );

        if (res && res.status === 200) {
          await this.fetchCategories();
          this.updateDialog = false;
          this.name = "";
        }
      }
    },

    async deleteCategory(id) {
      const res = await http.del(`/api/admin/categories/delete/${id}`);
      if (res && res.status === 200) {
        await this.fetchCategories();
        this.deleteDialog = false;
      }
    },

    onClickCreateBtn() {
      this.createDialog = true;
    },

    onClickUpdateBtn(item) {
      this.updateDialog = true;
      this.toUpdateId = item.id;
      this.name = item.name;
    },

    onClickDeleteBtn(item) {
      this.deleteDialog = true;
      this.selectItem = item;
    },
  },
};
</script>