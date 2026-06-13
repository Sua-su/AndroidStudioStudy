import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.data.repository.MovieRepository
import com.example.tmdb.model.Achievement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    private val _nickname = MutableStateFlow<String?>(null)
    val nickname: StateFlow<String?> = _nickname

    val achievements: StateFlow<List<Achievement>> = repository.getAllAchievements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
...

    fun setNickname(name: String) {
        _nickname.value = if (name.isBlank()) null else name
    }

    fun logout() {
        _nickname.value = null
    }
}
