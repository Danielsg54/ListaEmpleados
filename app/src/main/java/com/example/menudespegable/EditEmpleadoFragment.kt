package com.example.menudespegable

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import com.example.menudespegable.adapter.EmpleadoAdapter
import com.example.menudespegable.entity.Empleado
import com.example.menudespegable.repository.EmpleadoRepository
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
const val PICK_IMAGE = 100

/**
 * A simple [Fragment] subclass.
 * Use the [EditEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var empleado: Empleado? = null
    lateinit var img_avatar : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            empleado = it.get(ARG_PARAM1) as Empleado?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_edit_empleado, container, false)

        val text = view.findViewById<TextView>(R.id.empleado)
        text.setText(empleado?.nombre)


        val textNombre = view.findViewById<EditText>(R.id.txtNombre)
        val textIdentificacion = view.findViewById<EditText>(R.id.txtIdentificacion)
        val textPuesto = view.findViewById<EditText>(R.id.txtPuesto)
        val textDepartamento = view.findViewById<EditText>(R.id.txtDepartamento)
        img_avatar  = view.findViewById(R.id.avatar)

        textNombre.setText(empleado?.nombre)
        textIdentificacion.setText(empleado?.identificacion)
        textPuesto.setText(empleado?.puesto)
        textDepartamento.setText(empleado?.departamento)

        if(empleado?.avatar != ""){

            img_avatar.setImageBitmap(empleado?.avatar?.let { decodeImage(it) })
        }

        val modificar = view.findViewById<Button>(R.id.btnModificar)
        val eliminar = view.findViewById<Button>(R.id.btnEliminar)

        val fragmento: Fragment = CamaraFragment.newInstance(getString(R.string.menu_camara))


        modificar.setOnClickListener {

            val builder = AlertDialog.Builder(context)

            builder.setMessage("??Desea modificar el registro?")
                .setCancelable(false)
                .setPositiveButton("S??") { dialog, id ->

                    empleado?.identificacion = textIdentificacion.text.toString()
                    empleado?.nombre = textNombre.text.toString()
                    empleado?.puesto = textPuesto.text.toString()
                    empleado?.departamento = textDepartamento.text.toString()
                    empleado?.avatar = encodeImage(img_avatar.drawable.toBitmap())!!

                    empleado?.let { it1 -> EmpleadoRepository.instance.edit(it1) }


                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                    activity?.setTitle("Empleados")
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id ->
                    // logica del no
                }
            val alert = builder.create()
            alert.show()


        }

        eliminar.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setMessage("??Desea eliminar el registro?")
                .setCancelable(false)
                .setPositiveButton("S??") { dialog, id ->

                    empleado?.identificacion = textIdentificacion.text.toString()
                    empleado?.nombre = textNombre.text.toString()
                    empleado?.puesto = textPuesto.text.toString()
                    empleado?.departamento = textDepartamento.text.toString()

                    empleado?.let { it1 -> EmpleadoRepository.instance.delete(it1) }

                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                    activity?.setTitle("Empleados")
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id ->
                    // logica del no
                }
            val alert = builder.create()
            alert.show()


        }


        img_avatar.setOnClickListener {
            var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,PICK_IMAGE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            var imageUri = data?.data

            Picasso.get()
                .load(imageUri)
                .resize(150, 150)
                .centerCrop()
                .into(img_avatar)
        }
    }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT).replace("\n","")
    }

    private fun decodeImage (b64 : String): Bitmap{
        val imageBytes = Base64.decode(b64, 0)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param empleado Parameter 1.
         * @return A new instance of fragment EditEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(empleado: Empleado) =
            EditEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, empleado)
                }
            }
    }
}