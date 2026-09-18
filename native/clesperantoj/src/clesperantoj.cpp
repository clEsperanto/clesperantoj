#include "clesperantoj.hpp"

#include <sstream>

void BackendJ::setBackend(const std::string &backendName)
{
    if (backendName.find("cuda") != std::string::npos)
    {
        cle::BackendManager::getInstance().setBackend("cuda");
    }
    else
    {
        cle::BackendManager::getInstance().setBackend("opencl");
    }
}

DeviceJ::DeviceJ()
{
    this->device_ = cle::BackendManager::getInstance().getBackend().getDevice("", "all");
}

DeviceJ::DeviceJ(const std::shared_ptr<cle::Device> &device) : device_(device)
{
}

bool DeviceJ::operator==(const DeviceJ &other) const
{
    return (device_ == other.device_);
}

std::vector<std::string> DeviceJ::getAvailableDevices(const std::string &deviceType)
{
    return cle::BackendManager::getInstance().getBackend().getDevicesList(deviceType);
}

void DeviceJ::setDevice(const std::string &deviceName, const std::string &deviceType)
{
    this->device_ = cle::BackendManager::getInstance().getBackend().getDevice(deviceName, deviceType);
}

std::string DeviceJ::getName() const
{
    return this->device_->getName();
}

std::string DeviceJ::getInfo() const
{
    return this->device_->getInfo();
}

void DeviceJ::setWaitForKernelFinish(bool flag)
{
    this->device_->setWaitToFinish(flag);
}

std::shared_ptr<cle::Device> DeviceJ::get() const
{
    return this->device_;
}

ArrayJ::ArrayJ(const std::shared_ptr<cle::Array> &array) : array_(array)
{
}

size_t ArrayJ::getWidth() const
{
    return this->array_->width();
}

size_t ArrayJ::getHeight() const
{
    return this->array_->height();
}

size_t ArrayJ::getDepth() const
{
    return this->array_->depth();
}

unsigned int ArrayJ::getDimension() const
{
    return this->array_->dimension();
}

std::shared_ptr<cle::Array> ArrayJ::get() const
{
    return this->array_;
}

void ArrayJ::fillMemory(float value)
{
    this->array_->fill(value);
}

void ArrayJ::copyDataTo(ArrayJ &dst)
{
    this->array_->copyTo(dst.get());
}

std::vector<std::string> UtilsJ::getKeys(const std::unordered_map<std::string, std::vector<float>> &map)
{
    std::vector<std::string> keys;
    keys.reserve(map.size());
    std::transform(map.begin(), map.end(), std::back_inserter(keys),
                   [](const auto &element)
                   { return element.first; });
    return keys;
}

std::vector<ArrayJ> UtilsJ::toArrayJVector(const std::vector<std::shared_ptr<cle::Array>> &arr)
{
    std::vector<ArrayJ> result(arr.size());
    std::transform(arr.begin(), arr.end(), std::back_inserter(result), [](const std::shared_ptr<cle::Array> &value)
                   { return ArrayJ{value}; });
    return result;
}

inline cle::dType to_cle_dType(const DTypeJ &dtype)
{
    switch (dtype)
    {
    case DTypeJ::INT8:
        return cle::dType::INT8;
    case DTypeJ::UINT8:
        return cle::dType::UINT8;
    case DTypeJ::INT16:
        return cle::dType::INT16;
    case DTypeJ::UINT16:
        return cle::dType::UINT16;
    case DTypeJ::INT32:
        return cle::dType::INT32;
    case DTypeJ::UINT32:
        return cle::dType::UINT32;
    case DTypeJ::FLOAT:
        return cle::dType::FLOAT;
    case DTypeJ::UNKNOWN:
    default:
        return cle::dType::UNKNOWN;
    }
}

inline DTypeJ from_cle_dType(const cle::dType &dtype)
{
    switch (dtype)
    {
    case cle::dType::INT8:
        return DTypeJ::INT8;
    case cle::dType::UINT8:
        return DTypeJ::UINT8;
    case cle::dType::INT16:
        return DTypeJ::INT16;
    case cle::dType::UINT16:
        return DTypeJ::UINT16;
    case cle::dType::INT32:
        return DTypeJ::INT32;
    case cle::dType::UINT32:
        return DTypeJ::UINT32;
    case cle::dType::FLOAT:
        return DTypeJ::FLOAT;
        //        case cle::dType::UNKNOWN: // TODO: uncomment after https://github.com/clEsperanto/CLIc/pull/353 is merged, released, and we depend on that version
    default:
        return DTypeJ::UNKNOWN;
    }
}

inline cle::mType to_cle_mType(const MTypeJ &mtype)
{
    switch (mtype)
    {
    case MTypeJ::IMAGE:
        return cle::mType::IMAGE;
    case MTypeJ::BUFFER:
    default:
        return cle::mType::BUFFER;
    }
}

inline MTypeJ from_cle_mType(const cle::mType &mtype)
{
    switch (mtype)
    {
    case cle::mType::IMAGE:
        return MTypeJ::IMAGE;
    case cle::mType::BUFFER:
    default:
        return MTypeJ::BUFFER;
    }
}

ArrayJ ArrayJ::create(
    const size_t &width,
    const size_t &height,
    const size_t &depth,
    const size_t &dimension,
    const DTypeJ &data_type,
    const MTypeJ &memory_type,
    const DeviceJ &device)
{
    auto data = cle::Array::create(width, height, depth, dimension, to_cle_dType(data_type), to_cle_mType(memory_type), device.get());
    return ArrayJ{data};
}

void ArrayJ::writeFrom(
    void *data,
    const size_t &origin_x,
    const size_t &origin_y,
    const size_t &origin_z,
    const size_t &width,
    const size_t &height,
    const size_t &depth) const
{
    std::array<size_t, 3> origin = {origin_x, origin_y, origin_z};
    std::array<size_t, 3> region = {width, height, depth};
    this->array_->writeFrom(data, region, origin);
}

void ArrayJ::readTo(
    void *data,
    const size_t &origin_x,
    const size_t &origin_y,
    const size_t &origin_z,
    const size_t &width,
    const size_t &height,
    const size_t &depth) const
{
    std::array<size_t, 3> origin = {origin_x, origin_y, origin_z};
    std::array<size_t, 3> region = {width, height, depth};
    this->array_->readTo(data, region, origin);
}

DTypeJ ArrayJ::dtype() const
{
    return from_cle_dType(this->array_->dtype());
}

MTypeJ ArrayJ::mtype() const
{
    return from_cle_mType(this->array_->mtype());
}

DeviceJ ArrayJ::device() const
{
    return DeviceJ{this->array_->device()};
}

void MemoryJ::readToFloat(const ArrayJ &array, float *data, const size_t &origin_x, const size_t &origin_y, const size_t &origin_z, const size_t &width, const size_t &height, const size_t &depth)
{
    array.readTo(static_cast<void *>(data), origin_x, origin_y, origin_z, width, height, depth);
}

void MemoryJ::readToByte(const ArrayJ &array, char *data, const size_t &origin_x, const size_t &origin_y, const size_t &origin_z, const size_t &width, const size_t &height, const size_t &depth)
{
    array.readTo(static_cast<void *>(data), origin_x, origin_y, origin_z, width, height, depth);
}

void MemoryJ::readToShort(const ArrayJ &array, short *data, const size_t &origin_x, const size_t &origin_y, const size_t &origin_z, const size_t &width, const size_t &height, const size_t &depth)
{
    array.readTo(static_cast<void *>(data), origin_x, origin_y, origin_z, width, height, depth);
}

void MemoryJ::readToInt(const ArrayJ &array, int *data, const size_t &origin_x, const size_t &origin_y, const size_t &origin_z, const size_t &width, const size_t &height, const size_t &depth)
{
    array.readTo(static_cast<void *>(data), origin_x, origin_y, origin_z, width, height, depth);
}

void MemoryJ::writeFromFloat(const ArrayJ &array, float *data, const size_t &origin_x, const size_t &origin_y, const size_t &origin_z, const size_t &width, const size_t &height, const size_t &depth)
{
    array.writeFrom(static_cast<void *>(data), origin_x, origin_y, origin_z, width, height, depth);
}

void MemoryJ::writeFromByte(const ArrayJ &array, char *data, const size_t &origin_x, const size_t &origin_y, const size_t &origin_z, const size_t &width, const size_t &height, const size_t &depth)
{
    array.writeFrom(static_cast<void *>(data), origin_x, origin_y, origin_z, width, height, depth);
}

void MemoryJ::writeFromShort(const ArrayJ &array, short *data, const size_t &origin_x, const size_t &origin_y, const size_t &origin_z, const size_t &width, const size_t &height, const size_t &depth)
{
    array.writeFrom(static_cast<void *>(data), origin_x, origin_y, origin_z, width, height, depth);
}

void MemoryJ::writeFromInt(const ArrayJ &array, int *data, const size_t &origin_x, const size_t &origin_y, const size_t &origin_z, const size_t &width, const size_t &height, const size_t &depth)
{
    array.writeFrom(static_cast<void *>(data), origin_x, origin_y, origin_z, width, height, depth);
}

AffineTransformJ::AffineTransformJ() : transform_()
{
}

AffineTransformJ::AffineTransformJ(std::vector<float> *transform_matrix)
{
    if (transform_matrix == nullptr)
    {
        transform_ = cle::transform::AffineTransform();
        return;
    }
    if (transform_matrix->size() != 16 && transform_matrix->size() != 9)
    {
        throw std::runtime_error("Error: Transformation matrix size must be 9 or 16.");
    }
    std::array<float, 16> transform_matrix_arr;
    if (transform_matrix->size() == 9)
    {
        transform_matrix_arr = {(*transform_matrix)[0],
                                 (*transform_matrix)[1],
                                 0,
                                 (*transform_matrix)[2],
                                 (*transform_matrix)[3],
                                 (*transform_matrix)[4],
                                 0,
                                 (*transform_matrix)[5],
                                 (*transform_matrix)[6],
                                 (*transform_matrix)[7],
                                 1,
                                 0,
                                 0,
                                 0,
                                 0,
                                 1};
    }
    else
    {
        std::copy(transform_matrix->begin(), transform_matrix->end(), transform_matrix_arr.begin());
    }
    transform_ = cle::transform::AffineTransform(transform_matrix_arr);
}

void AffineTransformJ::scale(float scale_x, float scale_y, float scale_z)
{
    transform_.scale(scale_x, scale_y, scale_z);
}

void AffineTransformJ::rotate(int axis, float angle_deg)
{
    transform_.rotate(axis, angle_deg);
}

void AffineTransformJ::rotateAroundXAxis(float angle_deg)
{
    transform_.rotate_around_x_axis(angle_deg);
}

void AffineTransformJ::rotateAroundYAxis(float angle_deg)
{
    transform_.rotate_around_y_axis(angle_deg);
}

void AffineTransformJ::rotateAroundZAxis(float angle_deg)
{
    transform_.rotate_around_z_axis(angle_deg);
}

void AffineTransformJ::translate(float translate_x, float translate_y, float translate_z)
{
    transform_.translate(translate_x, translate_y, translate_z);
}

void AffineTransformJ::center(std::vector<int> shape, bool undo)
{
    std::array<size_t, 3> shape_arr = {1, 1, 1};
    for (size_t i = 0; i < shape.size() && i < 3; ++i)
    {
        shape_arr[i] = static_cast<size_t>(shape[i]);
    }
    transform_.center(shape_arr, undo);
}

void AffineTransformJ::shearInXPlane(float shear_y_deg, float shear_z_deg)
{
    transform_.shear_in_x_plane(shear_y_deg, shear_z_deg);
}

void AffineTransformJ::shearInYPlane(float shear_x_deg, float shear_z_deg)
{
    transform_.shear_in_y_plane(shear_x_deg, shear_z_deg);
}

void AffineTransformJ::shearInZPlane(float shear_x_deg, float shear_y_deg)
{
    transform_.shear_in_z_plane(shear_x_deg, shear_y_deg);
}

void AffineTransformJ::deskewX(float angle_deg, float voxel_size_x, float voxel_size_y, float voxel_size_z, float scale_factor)
{
    transform_.deskew_x(angle_deg, voxel_size_x, voxel_size_y, voxel_size_z, scale_factor);
}

void AffineTransformJ::deskewY(float angle_deg, float voxel_size_x, float voxel_size_y, float voxel_size_z, float scale_factor)
{
    transform_.deskew_y(angle_deg, voxel_size_x, voxel_size_y, voxel_size_z, scale_factor);
}

std::vector<float> AffineTransformJ::getMatrix() const
{
    auto array = cle::transform::AffineTransform::toArray(transform_.getMatrix());
    return std::vector<float>(array.begin(), array.end());
}

cle::transform::AffineTransform AffineTransformJ::get() const
{
    return transform_;
}
