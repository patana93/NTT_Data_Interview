import com.google.gson.annotations.SerializedName

/**
 * Competition team details
 * @param id
 * @param name
 * @param code
 * @param emblemUrl
 */
data class Competition (
	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("code") val code : String,
	@SerializedName("emblemUrl") val emblemUrl : String,
)