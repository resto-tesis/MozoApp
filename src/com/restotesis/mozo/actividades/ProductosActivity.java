package com.restotesis.mozo.actividades;

import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.restotesis.mozo.R;
import com.restotesis.mozo.adaptadores.ProductoAdapter;
import com.restotesis.mozo.networking.Conexion;
import com.restotesis.mozo.representacion.Eleccion;
import com.restotesis.mozo.representacion.Pedido;
import com.restotesis.mozo.representacion.Producto;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ProductosActivity extends Activity {

	private ListView lstView;
	private ArrayList<Producto> arregloProductos;
	private ArrayList<Eleccion> arregloElecciones;
	private ProductoAdapter adaptadorProductos;
	private Pedido unPedido;
	private RequestQueue colaSolicitud;
	private int PEDIDO_MODIFICADO = 1;
	private int PEDIDO_SINMODIFICAION = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productos);
		colaSolicitud = Conexion.getInstance(getApplicationContext())
				.getRequestQueue();
		arregloProductos = new ArrayList<Producto>();
		unPedido = getIntent().getExtras().getParcelable("elPedido");
		arregloProductos = unPedido.getListaProductos();
		adaptadorProductos = new ProductoAdapter(getApplicationContext(),
				arregloProductos);
		lstView = (ListView) findViewById(R.id.listProductos);
		lstView.setAdapter(adaptadorProductos);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.productos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		arregloElecciones = new ArrayList<Eleccion>();
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		if (id == R.id.bebidas) {
			JsonObjectRequest solicitudEleccion = new JsonObjectRequest(
					Request.Method.GET, unPedido.getUrlPedirBebidas(), null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							parseJSONChoices(response);
							Bundle bundle = new Bundle();
							bundle.putParcelableArrayList("listaElecciones",
									arregloElecciones);
							bundle.putParcelable("elPedido", unPedido);
							Intent intent = new Intent(ProductosActivity.this,
									EleccionActivity.class);
							intent.putExtras(bundle);
							System.out.println("entroooo");
							startActivityForResult(intent, 0);

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							System.out.println("No entrooo");
						}
					}) {

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					// TODO Auto-generated method stub
					return Conexion.createBasicAuthHeader();
				}

			};
			colaSolicitud.add(solicitudEleccion);

			return true;
		}
		if (id == R.id.eliminarBebidas){
			JsonObjectRequest solicitudEleccion = new JsonObjectRequest(
					Request.Method.GET, unPedido.getUrlRemoveFromBebidas(), null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							parseJSONRemoveChoices(response);
							Bundle bundle = new Bundle();
							bundle.putParcelableArrayList("listaElecciones",
									arregloElecciones);
							bundle.putParcelable("elPedido", unPedido);
							Intent intent = new Intent(ProductosActivity.this,
									EleccionActivity.class);
							intent.putExtras(bundle);
							System.out.println("entroooo");
							startActivityForResult(intent, 0);

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							System.out.println("No entrooo");
						}
					}) {

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					// TODO Auto-generated method stub
					return Conexion.createBasicAuthHeader();
				}

			};
			colaSolicitud.add(solicitudEleccion);
			
		}
		if (id == R.id.menues) {

			return true;

		}

		if (id == R.id.entrada) {

			return true;

		}
		if (id == R.id.principal) {

			return true;

		}

		if (id == R.id.guarnicion) {

			return true;

		}

		if (id == R.id.postre) {
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case (0): {
			if (resultCode == Activity.RESULT_OK) {
				unPedido = data.getExtras().getParcelable(
						"pedidoActualizado");				
				adaptadorProductos.actualizar(unPedido.getListaProductos());
				adaptadorProductos.notifyDataSetChanged();				
				Bundle bundle = new Bundle();
				bundle.putParcelable("pedidoActualizado",unPedido);
				bundle.putInt("posicion", getIntent().getExtras().getInt("posicion"));
				Intent intentRegreso = new Intent();
				intentRegreso.putExtras(bundle);
				setResult(PEDIDO_MODIFICADO,intentRegreso);
				System.out.println("Result OK");
			}
			break;
		}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void parseJSONChoices(JSONObject json) {
		try {

			JSONArray links = json.getJSONArray("links");
			String invokeURL = links.getJSONObject(2).getString("href")
					.toString();
			JSONObject parameters = json.getJSONObject("parameters");
			JSONArray temp = parameters.names();
			JSONObject item = null;
			String tipo = null;
			if (temp.toString().contains("bebida1")) {
				item = parameters.getJSONObject("bebida1");
				tipo = "bebida1";
			}
			if (temp.toString().contains("platoEntrada1")) {
				item = parameters.getJSONObject("platoEntrada1");
				tipo = "platoEntrada1";
			}
			if (temp.toString().contains("platoPrincipal1")) {
				item = parameters.getJSONObject("platoPrincipal1");
				tipo = "platoPrincipal1";
			}
			if (temp.toString().contains("postre1")) {
				item = parameters.getJSONObject("postre1");
				tipo = "postre1";
			}
			if (temp.toString().contains("guarnición1")) {
				item = parameters.getJSONObject("guarnición1");
				tipo = "guarnición1";
			}
			if (temp.toString().contains("menu1")) {
				item = parameters.getJSONObject("menu1");
				tipo = "menu1";
			}

			JSONArray choices = item.getJSONArray("choices");
			for (int i = 0; i < choices.length(); i++) {
				JSONObject unaOpcion = choices.getJSONObject(i);
				Eleccion unaEleccion = new Eleccion();
				unaEleccion.setTitle(unaOpcion.optString("title"));
				unaEleccion.setUrl(unaOpcion.optString("href"));
				unaEleccion.setTipo(tipo);
				unaEleccion.setInvokeURL(invokeURL);
				arregloElecciones.add(unaEleccion);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parseJSONRemoveChoices(JSONObject json) {
		try {

			JSONArray links = json.getJSONArray("links");
			String invokeURL = links.getJSONObject(2).getString("href")
					.toString();
			JSONObject parameters = json.getJSONObject("parameters");
			JSONArray temp = parameters.names();
			JSONObject item = null;
			String tipo = null;
			if (temp.toString().contains("bebida")) {
				item = parameters.getJSONObject("bebida");
				tipo = "bebida";
			}
			if (temp.toString().contains("platoEntrada")) {
				item = parameters.getJSONObject("platoEntrada");
				tipo = "platoEntrada";
			}
			if (temp.toString().contains("platoPrincipal")) {
				item = parameters.getJSONObject("platoPrincipal");
				tipo = "platoPrincipal";
			}
			if (temp.toString().contains("postre")) {
				item = parameters.getJSONObject("postre");
				tipo = "postre";
			}
			if (temp.toString().contains("guarnición")) {
				item = parameters.getJSONObject("guarnición");
				tipo = "guarnición";
			}
			if (temp.toString().contains("menu")) {
				item = parameters.getJSONObject("menu");
				tipo = "menu";
			}

			JSONArray choices = item.getJSONArray("choices");
			for (int i = 0; i < choices.length(); i++) {
				JSONObject unaOpcion = choices.getJSONObject(i);
				Eleccion unaEleccion = new Eleccion();
				unaEleccion.setTitle(unaOpcion.optString("title"));
				unaEleccion.setUrl(unaOpcion.optString("href"));
				unaEleccion.setTipo(tipo);
				unaEleccion.setInvokeURL(invokeURL);
				arregloElecciones.add(unaEleccion);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
