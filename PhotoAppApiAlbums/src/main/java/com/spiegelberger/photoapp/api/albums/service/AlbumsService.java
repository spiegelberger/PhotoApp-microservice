package com.spiegelberger.photoapp.api.albums.service;


import java.util.List;

import com.spiegelberger.photoapp.api.albums.data.AlbumEntity;

public interface AlbumsService {
    List<AlbumEntity> getAlbums(String userId);
}
