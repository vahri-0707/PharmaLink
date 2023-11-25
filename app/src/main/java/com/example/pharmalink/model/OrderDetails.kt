package com.example.pharmalink.model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

class OrderDetails(): Parcelable {
    var userId: String? = null
    var userName: String? = null
    var drugNames: MutableList<String>? = null
    var drugImages: MutableList<String>? = null
    var drugPrices: MutableList<String>? = null
    var drugQuantities: MutableList<Int>? = null
    var address: String? = null
    var totalPrice: String? = null
    var phoneNumber: String? = null
    var orderAccepted: Boolean = false
    var paymentReceived: Boolean = false
    var itemPushKey: String? = null
    var currentTime: Long = 0

    constructor(parcel: Parcel) : this() {
        userId = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        name: String,
        drugItemName: ArrayList<String>,
        drugItemPrice: ArrayList<String>,
        drugItemImage: ArrayList<String>,
        drugItemQuantities: ArrayList<Int>,
        address: String,
        totalAmount: String,
        phone: String,
        time: Long,
        itemPushKey: String?,
        b: Boolean,
        b1: Boolean
    ) : this(){
        this.userId = userId
        this.userName = name
        this.drugNames = drugItemName
        this.drugPrices = drugItemPrice
        this.drugImages = drugItemImage
        this.drugQuantities = drugItemQuantities
        this.address = address
        this.totalPrice = totalAmount
        this.phoneNumber = phone
        this.currentTime = time
        this.itemPushKey = itemPushKey
        this.orderAccepted = orderAccepted
        this.paymentReceived = paymentReceived
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }

}