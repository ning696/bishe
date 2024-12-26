<script setup>
import zh from 'element-plus/es/locale/lang/zh-cn.mjs'
</script>

<template>
  <header>
    <div>
      <el-config-provider :locale="zh">
        <router-view />
      </el-config-provider>
    </div>
  </header>
</template>

<style scoped lang="scss">
header {
  line-height: 1.5;
  max-height: 100vh;
  --el-color-primary:#32c5ff;
}
:deep(.el-button){
  &.red span{
    color: #FD4C40;
  
  }
}
:deep(.el-form){
  .el-form-item{
    margin-right: 10px;
  }
  .el-form-item__label{
    width: 70px;
    text-align: left;
    justify-content: flex-start;
    color: #222;
    font-weight: normal;
    display: none;
  }
  .el-select,.el-input{
    width: 180px !important;
    .el-input__wrapper,.el-select__wrapper{
      font-size: 13px;
    }
  }
  .el-button{
    span{
      font-size: 13px;
    }
  }
}
:deep(.el-pagination){
  .el-select .el-select__wrapper,.el-input .el-input__wrapper{
    height: 24px;
  }
}
</style>