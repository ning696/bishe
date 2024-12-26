// store/modules/cart.js

import { listItem, addToCartApi, addcartquantity, delItemquantity } from '../../api/cartitem'

const state = {
  cartItems: [] // 购物车商品列表
}

const mutations = {
  SET_CART_ITEMS(state, items) {
    state.cartItems = items
  },
  ADD_CART_ITEM(state, item) {
    const existingItem = state.cartItems.find(cartItem => cartItem.id === item.id)
    if (existingItem) {
      existingItem.quantity++
    } else {
      state.cartItems.push({ ...item, quantity: 1 })
    }
  },
  UPDATE_CART_ITEM_QUANTITY(state, { itemId, quantity }) {
    const item = state.cartItems.find(cartItem => cartItem.id === itemId)
    if (item) {
      item.quantity = quantity
    }
  },
  REMOVE_CART_ITEM(state, itemId) {
    state.cartItems = state.cartItems.filter(item => item.id !== itemId)
  }
}

const actions = {
  async loadCartItems({ commit }) {
    const response = await listItem()
    if (response.data.code === 20000) {
      commit('SET_CART_ITEMS', response.data.data)
    }
  },
  async addToCart({ commit }, item) {
    const response = await addToCartApi(item.id)
    if (response.data.code === 20000) {
      commit('ADD_CART_ITEM', item)
    }
  },
  async increaseCartItemQuantity({ commit }, itemId) {
    await addcartquantity(itemId)
    const response = await listItem()
    commit('SET_CART_ITEMS', response.data.data)
  },
  async decreaseCartItemQuantity({ commit }, itemId) {
    await delItemquantity(itemId)
    const response = await listItem()
    commit('SET_CART_ITEMS', response.data.data)
  }
}

const getters = {
  cartItems: state => state.cartItems,
  cartCount: state => state.cartItems.reduce((count, item) => count + item.quantity, 0),
  totalPrice: state => state.cartItems.reduce((total, item) => total + item.salePrice * item.quantity, 0)
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}
