package com.example.musicapp.ui.home.album;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.album.Album;
import com.example.musicapp.data.model.album.AlbumList;
import com.example.musicapp.data.repository.album.AlbumRepository;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class AlbumViewModel extends ViewModel {
    // LiveData để quản lý danh sách album
    private final MutableLiveData<List<Album>> mAlbumList = new MutableLiveData<>();
    // Repository để truy cập dữ liệu album
    private AlbumRepository mAlbumRepository;

    @Inject
    public AlbumViewModel(AlbumRepository albumRepository) {
        mAlbumRepository = albumRepository;
        loadAlbumList();
    }

    // Đặt giá trị mới cho danh sách album
    public void setAlbumList(List<Album> albumList) {
        mAlbumList.postValue(albumList);
    }

    // Lấy danh sách album từ LiveData
    public LiveData<List<Album>> getAlbumList() {
        return mAlbumList;
    }

    // Tải danh sách album từ nguồn dữ liệu và cập nhật giá trị cho LiveData
    private void loadAlbumList() {
        // Thực hiện logic để tải danh sách album từ nguồn dữ liệu
        // Sau đó cập nhật giá trị cho mAlbumList
        mAlbumRepository.loadAlbums(new Callback<AlbumList>() {
            @Override
            public void onResponse(@NonNull Call<AlbumList> call, @NonNull Response<AlbumList> response) {
                AlbumList albumList = response.body();
                if (albumList != null) {
//                    List<Album> albums = albumList.getAlbums();
//                    albums.sort(Comparator.comparing(albumList.size()));
//                    setAlbumList(Lists.reverse(albums));
                    setAlbumList(albumList.getAlbums());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AlbumList> call, @NonNull Throwable throwable) {
                // Khi thất bại, cập nhật danh sách album thành một danh sách trống
                mAlbumList.postValue(new ArrayList<>());
            }
        });
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final AlbumRepository mAlbumRepository;

        @Inject
        public Factory(AlbumRepository albumRepository) {
            mAlbumRepository = albumRepository;
        }

        @NonNull
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AlbumViewModel.class)) {
                return (T) new AlbumViewModel(mAlbumRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
