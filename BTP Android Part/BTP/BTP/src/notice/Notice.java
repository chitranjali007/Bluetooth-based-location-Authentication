package notice;

import android.content.Context;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Notice {

	@Inject
	private Context context;

	private Toast toast;

	public void showToast(String mensagem) {
		if (toast != null) {
			toast.setText(mensagem);
		} else {
			toast = Toast.makeText(context, mensagem, Toast.LENGTH_LONG);
		}
		toast.show();
	}

	public void closeToast() {
		if (toast != null)
			toast.cancel();
	}

}