package com.example.cookbook

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_add_cook.*
import android.os.Build
import android.widget.ImageView
import androidx.navigation.Navigation
import java.io.ByteArrayOutputStream


class AddCookFragment : Fragment() {

    //Tanımlamalar:
    var chosenImage : Uri? = null
    var chosenBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_cook, container, false)
    }



    //Fragment içinde fonksiyon kullanacaksak onViewCreated içinde çağırmalıyız.
    //Bu, fragmentlara özel bir durumdur.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            save(it)
        }
        imageView.setOnClickListener {
            pickImage(it)
        }

        //Arguman kontrolü:
        arguments?.let {

            var info = AddCookFragmentArgs.fromBundle(it).info

            if(info == "cameFromMenu"){
                //yeni yemek eklemeye geldi
                cookNameText.setText("")
                cookRecipeText.setText("")
                button.visibility = View.VISIBLE
                val image = BitmapFactory.decodeResource(context?.resources, R.drawable.theaddsymbol)
                imageView.setImageBitmap(image)

            } else{
                //daha önce kaydedilen yemeği görmeye geldi
                button.visibility = View.INVISIBLE
                val chosenId = AddCookFragmentArgs.fromBundle(it).id
                context?.let {
                    try {
                        val db = it.openOrCreateDatabase("CookBookDatabase", Context.MODE_PRIVATE, null)
                        val cursor = db.rawQuery("SELECT * FROM cooks WHERE id=?", arrayOf(chosenId.toString()))

                        val cookNameIndex = cursor.getColumnIndex("cookname")
                        val cookRecipeIndex = cursor.getColumnIndex("cookrecipe")
                        val cookImageIndex = cursor.getColumnIndex("image")

                        while(cursor.moveToNext()){
                            cookNameText.setText(cursor.getString(cookNameIndex))
                            cookRecipeText.setText(cursor.getString(cookRecipeIndex))

                            val byteArray =  cursor.getBlob(cookImageIndex)
                            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                            imageView.setImageBitmap(bitmap)
                        }

                        cursor.close()

                    } catch (e:Exception){
                        e.printStackTrace()
                    }
                }

            }
        }

    }

    fun save(view: View){
        val cookName = cookNameText.text.toString()
        val recipeName = cookRecipeText.text.toString()

        if(chosenBitmap != null){
            val miniBitmap = createMiniBitmap(chosenBitmap!!,300)

            //Bitmap'i sqlite'a kaydederken veriye çevirmeliyiz:
            val outputStream = ByteArrayOutputStream()
            miniBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val imageByteArray = outputStream.toByteArray()

            //sql işlemleri:
            try {
                context?.let {

                    //database oluşturma:
                    val database = it.openOrCreateDatabase("CookBookDatabase", Context.MODE_PRIVATE, null)
                    database.execSQL("CREATE TABLE IF NOT  EXISTS cooks (id INTEGER PRIMARY KEY, cookname VARCHAR, cookrecipe VARCHAR, image BLOB)")

                    //verileri kaydetme:
                    val sql = "INSERT INTO cooks (cookname, cookrecipe, image) VALUES (?, ?, ?)"
                    val statement = database.compileStatement(sql)
                    statement.bindString(1, cookName)
                    statement.bindString(2, recipeName)
                    statement.bindBlob(3, imageByteArray)
                    statement.execute()
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        val action = AddCookFragmentDirections.actionAddCookFragmentToCookListFragment()
        Navigation.findNavController(view).navigate(action)

    }

    fun pickImage(view: View){

        //activity'i let diyerek nullability'den kurtarıyoruz:
        activity?.let {

            //ContextCompat, android versiyonları(api) arasındaki uyumsuzluğu giderir

            //PERMISSION_GRANTED, izin onaylandı demektir. Bu bloğun anlamı: İzin verilmedi ise, izin iste
            if(ContextCompat.checkSelfPermission(it.applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

            }
            //Bu bloğun anlamı: zaten izin onaylanmış, tekrar izin isteme ve direkt olayı yap
            else{

                val photoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(photoIntent,2)

            }
        }
    }

    //istenilen izinlerin sonuçları:
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ){
        if(requestCode == 1){
            if(grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val photoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(photoIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
     }

    //startactivity demiştik, şimdi onun sonuçlarını alıyoruz:
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            chosenImage = data.data
            try {
                //Uri'yı bitmap'e çevirme
                if(chosenImage != null){
                    context?.let {
                        if(Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(it.contentResolver, chosenImage!!)
                            chosenBitmap = ImageDecoder.decodeBitmap(source)
                            imageView.setImageBitmap(chosenBitmap)
                        } else{
                            chosenBitmap = MediaStore.Images.Media.getBitmap(it.contentResolver,chosenImage)
                        }
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //Bitmap'in boyutu 1 mb'dan büyük olursa uygulama çökebilir. Bu yüzden bitmap'ı küçültme işlemi yaparız:
    fun createMiniBitmap(bitmapOfUser : Bitmap, maxSize : Int) : Bitmap {
        var width = bitmapOfUser.width
        var height = bitmapOfUser.height
        var bitmapProportion : Double = width.toDouble() / height.toDouble()

        //görsel yatay ise: genişliği, yüksekliğinden fazladır. w/h oranı 1'den büyük olur.
        //görsel dikey ise: yüksekliği, genişliğinden fazladır. w/h oranı 1'den küçük olur.

        if(bitmapProportion > 1){
            //görsel yatay
            width = maxSize
            val cutedHeight = width / bitmapProportion
            height = cutedHeight.toInt()
        } else{
            //görsel dikey
            height = maxSize
            val cutedWidth = height * bitmapProportion
            width = cutedWidth.toInt()
        }

        return Bitmap.createScaledBitmap(bitmapOfUser, width, height, true)
    }

}












